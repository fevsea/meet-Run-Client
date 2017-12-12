package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Awais Iqbal on 21/11/2017.
 */

public class PageServer<IServerModel> {
  @SerializedName("count")
  @Expose
  private int count;
  @SerializedName("next")
  @Expose
  private String next;
  @SerializedName("previous")
  @Expose
  private String previous;
  @SerializedName("results")
  @Expose
  private List<IServerModel> results;

  public List<IServerModel> getResults() {
    return results;
  }
}
