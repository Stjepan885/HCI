package com.example.myapplication;

        public class Users {
            private String userID;
            private String userText;
            private String ageText;
            private String phoneText;
            private String genderText;
            private String handText;
            private String swipeTime;
            private String zoomTime;
            private String swipeTimeG;
            private String zoomTimeG;
            private int locSwipeG = 0;
            private int locZoomG = 0;
            private int locSwipe = 0;
            private int locZoom = 0;

            public Users(){

            }

            public Users(String userID, String userText, String ageText, String phoneText, String genderText, String handText, String swipeTime, String zoomTime, String swipeTimeG, String zoomTimeG) {
                this.userID = userID;
                this.userText = userText;
                this.ageText = ageText;
                this.phoneText = phoneText;
                this.genderText = genderText;
                this.handText = handText;
                this.zoomTime = zoomTime;
                this.swipeTime = swipeTime;
            }

            public String getUserText() {
                return userText;
            }

            public String getAgeText() {
                return ageText;
            }

            public String getPhoneText() {
                return phoneText;
            }

            public String getGenderText() {
                return genderText;
            }

            public String getHandText() {
                return handText;
            }

            public String getSwipeTime() {
                return swipeTime;
            }

            public String getZoomTime() {
                return zoomTime;
            }

        }