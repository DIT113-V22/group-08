package com.quinstedt.grace;

public enum Direction {

    FORWARD{
        @Override
        public String toString() { return "1"; }

    },UPRIGHT{
        @Override
        public String toString() {
            return "2";
        }
    },RIGHT{
        @Override
        public String toString() { return "3";}
    },BACKRIGHT{
        @Override
        public String toString() {
            return "4";
        }
    }, REVERSE{
        @Override
        public String toString() { return "5"; }
    },BACKLEFT {
        @Override
        public String toString() { return "6";}
    },LEFT{
        @Override
        public String toString() { return "7"; }
    }, UPLEFT {
        @Override
        public String toString() { return "8"; }
    },STOP {
        @Override
        public String toString() { return "9"; }
    }
}
