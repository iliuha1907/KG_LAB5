package com.cg;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T,P> {
    T key;
    P value;

}
