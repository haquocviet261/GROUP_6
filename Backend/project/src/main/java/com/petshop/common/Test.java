package com.petshop.common;


import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public class Test {
    protected String name;
    Integer age;
    public Test() {
    }

    public Test(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Test(int age) {
        this.age = age;
    }
    public void test(int age){
       this.age =10;
    }

    public static void main(String[] args) {
        Test test = new Test();
        String s = "WITH DepositSummary AS (\n" +
                "  SELECT\n" +
                "    cd.id,\n" +
                "    SUM( dm.deposit_money ) AS depositMoney,\n" +
                "    MIN( dm.deposit_date ) AS depositDate \n" +
                "  FROM\n" +
                "    customer_transaction cd\n" +
                "    LEFT JOIN deposit_money dm ON cd.id = dm.customer_transaction_id \n" +
                "    AND dm.deleted_at IS NULL \n" +
                "  WHERE\n" +
                "    cd.created_at = ( SELECT MAX( created_at ) FROM customer_transaction WHERE customer_id = cd.customer_id AND deleted_at IS NULL ) \n" +
                "  GROUP BY\n" +
                "    cd.id \n" +
                "  ) SELECT\n" +
                "  cd.id,\n" +
                "  cd.customer_id AS customerId,\n" +
                "  cp.customer_name AS customerName,\n" +
                "  cp.date_of_birth AS dateOfBirth,\n" +
                "  cp.customer_phone AS customerPhone,\n" +
                "  cd.payment_status AS paymentStatus,\n" +
                "  cd.purchased_service_date AS purchasedServiceDate,\n" +
                "  cd.appointment_date AS appointmentDate,\n" +
                "  cd.service_booked_call_center AS serviceBookedCallCenter,\n" +
                "  cd.consultant_status AS consultantStatus,\n" +
                "  cd.customer_purchased_service AS customerPurchasedService,\n" +
                "  cd.discount,\n" +
                "  GROUP_CONCAT( pct.promotion_id SEPARATOR ', ' ) AS promotionId,\n" +
                "  cd.consultant_date AS consultantDate,\n" +
                "  cd.contract_date AS contractDate,\n" +
                "  cd.signing_filming_commitment_date AS signingFilmingCommitmentDate,\n" +
                "  cd.actual_birth_date AS actualBirthDate,\n" +
                "  cd.gestational_birth_age AS gestationalBirthAge,\n" +
                "  cd.transfer_status AS transferStatus,\n" +
                "  uc.full_name AS userConsultantName,\n" +
                "  ucc.full_name AS userCallCenterName,\n" +
                "  cd.total_price_service AS totalPriceService,\n" +
                "  cd.tc_code AS tcCode,\n" +
                "  cd.estimated_due_date AS estimatedDueDate,\n" +
                "  cd.customer_classification AS customerClassification,\n" +
                "  cd.birth_status AS birthStatus,\n" +
                "  cd.source_guest AS sourceGuest,\n" +
                "  cd.created_at AS createdAt,\n" +
                "  cd.reason,\n" +
                "  ds.depositMoney \n" +
                "FROM\n" +
                "  customer_transaction cd\n" +
                "  INNER JOIN customer_profile cp ON cd.customer_id = cp.id\n" +
                "  LEFT JOIN user uc ON uc.user_name = cd.user_consultant\n" +
                "  LEFT JOIN user ucc ON ucc.user_name = cd.user_call_center\n" +
                "  INNER JOIN DepositSummary ds ON cd.id = ds.id\n" +
                "  LEFT JOIN promotion_ref_customer_transaction pct ON cd.id = pct.customer_transaction_id \n" +
                "  AND pct.deleted_at IS NULL \n" +
                "WHERE\n" +
                "  cp.deleted_at IS NULL \n" +
                "  AND cd.deleted_at IS NULL \n" +
                "  AND cd.created_at BETWEEN '2024-05-01' \n" +
                "  AND '2024-09-13' \n" +
                "  AND (\n" +
                "    pct.promotion_id IS NULL \n" +
                "  OR pct.promotion_id IN ( 6, 22, 26 )) \n" +
                "GROUP BY\n" +
                "  cd.id,\n" +
                "  cp.customer_name,\n" +
                "  cp.date_of_birth,\n" +
                "  cp.customer_phone,\n" +
                "  cd.payment_status,\n" +
                "  cd.purchased_service_date,\n" +
                "  cd.appointment_date,\n" +
                "  cd.service_booked_call_center,\n" +
                "  cd.consultant_status,\n" +
                "  cd.customer_purchased_service,\n" +
                "  cd.discount,\n" +
                "  cd.consultant_date,\n" +
                "  cd.contract_date,\n" +
                "  cd.signing_filming_commitment_date,\n" +
                "  cd.actual_birth_date,\n" +
                "  cd.gestational_birth_age,\n" +
                "  cd.transfer_status,\n" +
                "  uc.full_name,\n" +
                "  ucc.full_name,\n" +
                "  cd.total_price_service,\n" +
                "  cd.tc_code,\n" +
                "  cd.estimated_due_date,\n" +
                "  cd.customer_classification,\n" +
                "  cd.birth_status,\n" +
                "  cd.source_guest,\n" +
                "  cd.created_at,\n" +
                "  cd.reason,\n" +
                "  ds.depositMoney \n" +
                "ORDER BY\n" +
                "  cd.created_at DESC;";
        List<Object> list = new ArrayList<>();
    }
}
