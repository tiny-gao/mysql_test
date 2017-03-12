package com.tinygao.mysql.gendata;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tinygao.util.GsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.applet.resources.MsgAppletViewer_sv;

import java.util.Map;

import static javafx.scene.input.KeyCode.T;

@Slf4j
@Data
public class Template {
    private final String filename;
    private final long num;
    private final long idFrom;
    private final Map<Integer, TypeElement> values;

    public String toString() {
        return GsonUtil.GSON.toJson(this);
    }
}
