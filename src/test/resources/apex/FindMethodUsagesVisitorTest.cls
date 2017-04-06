public virtual class MyClass {

    public virtual void method0() {
        System.debug('Writing some text.');
    }

    public virtual Double caller1() {
        methodToCall();
        return .05;
    } 

    public void method1(String str) {
        method2(int, str); //#result
    } 
    
    public void method1(Integer i) {
        method1(int);
    } 

    public void method2(Integer i, List<String> lst) { // method we look for
        method2(int, str); //#result
    } 

    public virtual Integer caller2() {
        method0();
        method2(int, str); //#result
        return 05;
    } 
}
