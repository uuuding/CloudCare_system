package com.cloudcare.entity;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.AbstractBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.ParameterStore;
import ai.djl.util.PairList;

public class GCNLayer extends AbstractBlock {

    private Linear linear;
    private NDArray adj;

    public GCNLayer(int inputDim, int outputDim, NDManager manager, NDArray adjMatrix) {
        super((byte) 2); // 2代表子层个数
        this.adj = adjMatrix;
        linear = Linear.builder().setUnits(outputDim).build();
        addChildBlock("linear", linear);
    }

    @Override
    protected NDList forwardInternal(ParameterStore ps, NDList inputs, boolean training, PairList<String, Object> params) {
        NDArray features = inputs.singletonOrThrow(); // X
        NDArray propagated = adj.dot(features);       // A·X
        NDList out = linear.forward(ps, new NDList(propagated), training); // A·X·W
        return out;
    }

    @Override
    public Shape[] getOutputShapes(Shape[] inputShapes) {
        return linear.getOutputShapes(inputShapes);
    }

    @Override
    public void initializeChildBlocks(NDManager manager, DataType dataType, Shape... inputShapes) {
        linear.initialize(manager, dataType, inputShapes);
    }
}
