public virtual class FindMethodUsagesVisitorTest {
    Integer int;
    List<String> _lst;
    String str;

    public virtual void method0() {
        System.debug('Writing some text.');
    }

    public virtual Double caller1() {
        methodToCall();
        return .05;
    } 

    public void method1(String str) {
        method2(int, _lst); //#findMethodUsages_result#
    } 
    
    public void method1(Integer i) {
        method1(int);
    } 

    public void method2(Integer i, List<String> lst) { // method we look for
        method2(int, _lst); //#findMethodUsages_result#
    } 

    public virtual Integer caller2() {
        method0();
        method2(int, _lst); //#findMethodUsages_result#
        return 05;
    } 

    public virtual Integer caller3() {
        method0();
        method2(int, str); 
        return 05;
    } 
}
