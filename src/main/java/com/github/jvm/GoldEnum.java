package com.github.jvm;

public enum GoldEnum {


    // enum implement
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

    /* define function */
    public abstract void print(Object print);


    public static void main(String... args) {

        GoldEnum.SYS_ERR.print("error");

        GoldEnum.SYS_OUT.print("right");
    }

}
