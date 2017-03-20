package com.neowit.apex.units

trait ApexUnit {
    def parentContext: Option[ApexUnit]
    //def children: List[ApexUnit]
}
