package com.smartstock.service;

import com.smartstock.model.Sales;
import com.smartstock.repository.SalesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Service
public class PreprocessingService {

    private final SalesRepository salesRepository;

    public PreprocessingService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }
    @Transactional
    public String preprocessData() {

        List<Sales> salesList =
                salesRepository.findAllByOrderByProductIdAscDateAsc();

        for (int i = 0; i < salesList.size(); i++) {

            Sales current = salesList.get(i);

            // -------------------------
            // Date Features
            // -------------------------

            current.setYear(current.getDate().getYear());

            current.setMonth(current.getDate().getMonthValue());

            current.setDay(current.getDate().getDayOfMonth());

            current.setDayOfWeek(
                    current.getDate().getDayOfWeek().getValue()
            );

            // -------------------------
            // Weekend
            // -------------------------

            DayOfWeek dayOfWeek =
                    current.getDate().getDayOfWeek();

            current.setIsWeekend(
                    dayOfWeek == DayOfWeek.SATURDAY ||
                            dayOfWeek == DayOfWeek.SUNDAY
            );

            // -------------------------
            // Lag 1
            // -------------------------

            if (i > 0 &&
                    sameProduct(current, salesList.get(i - 1))) {

                current.setSalesLag1(
                        salesList.get(i - 1).getSalesQuantity()
                );

            } else {

                current.setSalesLag1(null);
            }

            // -------------------------
            // Lag 7
            // -------------------------

            if (i >= 7 &&
                    sameProduct(current, salesList.get(i - 7))) {

                current.setSalesLag7(
                        salesList.get(i - 7).getSalesQuantity()
                );

            } else {

                current.setSalesLag7(null);
            }

            // -------------------------
            // Rolling 7-Day Average
            // -------------------------

            current.setSalesRolling7Mean(
                    calculateRollingAverage(
                            salesList,
                            i,
                            7,
                            current
                    )
            );

            // -------------------------
            // Rolling 30-Day Average
            // -------------------------

            current.setSalesRolling30Mean(
                    calculateRollingAverage(
                            salesList,
                            i,
                            30,
                            current
                    )
            );
        }

        salesRepository.saveAll(salesList);

        return salesList.size() +
                " sales records preprocessed successfully";
    }


    // Check whether two records belong to the same product
    private boolean sameProduct(Sales a, Sales b) {

        return a.getProductId().equals(b.getProductId());
    }


    // Calculate rolling average for the same product
    private Double calculateRollingAverage(
            List<Sales> salesList,
            int currentIndex,
            int window,
            Sales current) {

        int start =
                Math.max(0, currentIndex - window + 1);

        double total = 0;
        int count = 0;

        for (int i = start; i <= currentIndex; i++) {

            Sales previous = salesList.get(i);

            if (sameProduct(current, previous)) {

                total += previous.getSalesQuantity();
                count++;
            }
        }

        if (count == 0) {
            return 0.0;
        }

        return total / count;
    }
}