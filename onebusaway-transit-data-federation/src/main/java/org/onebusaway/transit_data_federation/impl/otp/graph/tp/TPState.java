package org.onebusaway.transit_data_federation.impl.otp.graph.tp;

import java.util.ArrayList;
import java.util.List;

import org.onebusaway.collections.tuple.Pair;
import org.onebusaway.transit_data_federation.bundle.tasks.transfer_pattern.TransferTree;
import org.onebusaway.transit_data_federation.services.transit_graph.StopEntry;

public class TPState {

  private final TPQueryData queryData;

  private final TransferTree tree;
  
  private final boolean isReverse;

  public static TPState start(TPQueryData queryData, TransferTree tree) {
    return new TPState(queryData, tree, false);
  }

  public static TPState end(TPQueryData queryData, TransferTree tree) {
    return new TPState(queryData, tree, true);
  }

  private TPState(TPQueryData queryData, TransferTree tree, boolean isReverse) {
    this.queryData = queryData;
    this.tree = tree;
    this.isReverse = isReverse;
  }

  public TPQueryData getQueryData() {
    return queryData;
  }

  public TransferTree getTree() {
    return tree;
  }
  
  public boolean isReverse() {
    return isReverse;
  }

  public Pair<StopEntry> getStops() {
    return tree.getStops();
  }

  public boolean hasTransfers() {
    return ! tree.getTransfers().isEmpty();
  }
  
  public boolean isExitAllowed() {
    return tree.isExitAllowed();
  }
  
  public List<TPState> getTransferStates() {
    List<TPState> next = new ArrayList<TPState>();
    for( TransferTree nextTree : tree.getTransfers() )
      next.add(new TPState(queryData, nextTree, isReverse));
    return next;
  }

  @Override
  public String toString() {
    return tree.getFromStop().getId() + " " + tree.getToStop().getId();
  }
}