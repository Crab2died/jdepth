package com.github.jvm;

public enum GoldEnum {


    SYS_OUT {
        @Override
        public void print(Object sysout) {
            System.out.println("this is system out right => " + sysout);
        }
    },
    SYS_ERR {
        @Override
        public void print(Object syserr) {
            System.err.println("this is system out error => " + syserr);
        }
    };

    public abstract void print(Object print);


    public static void main(String... args){
        GoldEnum.SYS_ERR.print("error");

        GoldEnum.SYS_OUT.print("right");
    }

}
