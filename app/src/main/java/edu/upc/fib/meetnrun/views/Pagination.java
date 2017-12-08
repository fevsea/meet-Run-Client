package edu.upc.fib.meetnrun.views;

import android.os.AsyncTask;

/**
 * Created by eric on 2/12/17.
 */

public interface Pagination {

    void initializePagination();

    void scrollListener();

    void setLoading();

    void updateData();

}
