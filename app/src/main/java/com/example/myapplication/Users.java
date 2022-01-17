package com.example.myapplication;

        public class Users {
            private String userID;
            private String userText;
            private String ageText;
            private String phoneText;
            private String genderText;
            private String handText;
            private int swipeTime;
            private int zoomTime;
            private int swipeTimeG = 0;
            private int zoomTimeG = 0;

            public Users(){

            }

            public Users(String userID, String userText, String ageText, String phoneText, String genderText, String handText, int swipeTime, int zoomTime) {
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

            public int getSwipeTime() {
                return swipeTime;
            }

            public int getZoomTime() {
                return zoomTime;
            }

        }