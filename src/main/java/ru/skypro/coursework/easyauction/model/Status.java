package ru.skypro.coursework.easyauction.model;

public enum Status {
        CREATED("Created"),
        STARTED("Started"),
        STOPPED("Stopped");

        private String status;

        Status(String status) {
                this.status = status;
        }

        public String getStatus() {
                return status;
        }
}
