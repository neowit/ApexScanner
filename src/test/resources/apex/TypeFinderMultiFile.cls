public virtual class TypeFinderMultiFile {
    

    private void findVarFromAnotherClass() {
        TypeFinder cls1 = new TypeFinder();
        System.debug(cls1.bool); // #findVarFromAnotherClass#
    }
    
    private void findVarFromInnerClassOfAnotherClass() {
        TypeFinder.InnerClass1 cls1 = new TypeFinder.InnerClass1();
        System.debug(cls1.innerStr1); // #findVarFromInnerClassOfAnotherClass#
    }

}
