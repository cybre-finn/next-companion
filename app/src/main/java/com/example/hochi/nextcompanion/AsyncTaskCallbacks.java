package com.example.hochi.nextcompanion;

interface AsyncTaskCallbacks<T> {
    void onTaskComplete(T response);
}
