package com.kurtalang.knowyourbrew.map;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kurtalang on 11/9/15.
 */
public interface PlaceListReceived{
    public void sendPlaceList(List<HashMap<String, String>> result);
}
