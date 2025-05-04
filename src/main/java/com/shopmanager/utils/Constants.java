package com.shopmanager.utils;

public class Constants {
    public static class OrderStatus {
        public static final String PENDING = "pending";
        public static final String PROCESSING = "processing";
        public static final String COMPLETED = "completed";
        public static final String CANCELLED = "cancelled";

        public static String[] getAllStatuses() {
            return new String[] { PENDING, PROCESSING, COMPLETED, CANCELLED };
        }
    }

    public static class PaymentStatus {
        public static final String UNPAID = "unpaid";
        public static final String PAID = "paid";
        public static final String REFUNDED = "refunded";

        public static String[] getAllStatuses() {
            return new String[] { UNPAID, PAID, REFUNDED };
        }
    }

    public static class PaymentMethod {
        public static final String CASH = "Tiền mặt";
        public static final String BANK_TRANSFER = "Chuyển khoản";
        public static final String MOMO = "Ví MoMo";
        public static final String VNPAY = "VNPay";

        public static String[] getAllMethods() {
            return new String[] { CASH, BANK_TRANSFER, MOMO, VNPAY };
        }
    }

    public static class TransactionType {
        public static final String IMPORT = "import";
        public static final String EXPORT = "export";
        public static final String ADJUSTMENT = "adjustment";

        public static String[] getAllTypes() {
            return new String[] { IMPORT, EXPORT, ADJUSTMENT };
        }
    }

    public static class UserRole {
        public static final String ADMIN = "admin";
        public static final String STAFF = "staff";

        public static String[] getAllRoles() {
            return new String[] { ADMIN, STAFF };
        }
    }
}