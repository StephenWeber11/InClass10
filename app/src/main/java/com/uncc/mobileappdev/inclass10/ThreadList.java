package com.uncc.mobileappdev.inclass10;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by StephenWeber on 4/4/2018.
 */

public class ThreadList implements Serializable {
    ArrayList<Thread> threads;

    public ArrayList<Thread> getThreads() {
        return threads;
    }

}
