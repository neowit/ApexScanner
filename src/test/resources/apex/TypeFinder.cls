public virtual class MyClass {
    
    private String str; //result of findClassVariableType
    private MyClass classInstance;

    public virtual void methodToCall() {
        System.debug('Writing some text.');
    }

    public virtual Double caller1() {
        methodToCall();
        return .05;
    } 

    public void findLocalVariableType(String str) {
        Integer int = 0; // result of findLocalVariableType
        methodWith2Params(int, str); //#findLocalVariableType
    } 

    public void findClassVariableType(String str) {
        Integer int = 0; 
        methodWith2Params(int, str); //#findClassVariableType
    } 

    public void findMethodType(String str) {
        Integer int = 0; 
        methodWith2Params(int, str); //#findMethodType  #findMethodType_int_str
    } 

    private M2Type methodWith2Params(Integer i, String s) { // #result of findMethodType
        String str;
        Integer int;
        
    }

    private M2Type methodWith2Params(String s1, String s2) { // #result of findMethodType
        String str;
        Integer int;
        
    }

}
