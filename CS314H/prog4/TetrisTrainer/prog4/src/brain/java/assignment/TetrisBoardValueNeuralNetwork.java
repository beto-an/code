package assignment;

import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.state.State;
import org.apache.commons.io.FilenameUtils;
import org.datavec.api.records.reader.BaseRecordReader;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.fetcher.BaseDataFetcher;
import org.nd4j.linalg.dataset.api.iterator.fetcher.DataSetFetcher;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TetrisBoardValueNeuralNetwork implements ValueFunction {

    private ComputationGraph model;

    public double predict(ArrayList<int[]> a) {
        int[][][] board = new int[1][20][10];
        for (int i = 0; i < 10; i++) {
            board[0][i] = a.get(i);
        }
        INDArray main_input = Nd4j.create(board);

        int[] prop = new int[27];
        int j = 0;
        for (int i = 0; i < 10; i++) {
            prop[j] = a.get(24)[i];
            j++;
        }
        for (int i = 0; i < 10; i++) {
            prop[j] = a.get(25)[i];
            j++;
        }
        for (int i = 0; i < 7; i++) {
            prop[j] = a.get(26)[i];
            j++;
        }
        INDArray properties = Nd4j.create(prop);
        INDArray input = Nd4j.hstack(main_input, properties);
        return model.output(input)[0].getDouble(0);


    }

    public void loadModel(String filename) throws Exception {
        final String SIMPLE_FUNCTIONAL_MLP = new File(filename).getAbsolutePath();
        model = KerasModelImport.importKerasModelAndWeights(SIMPLE_FUNCTIONAL_MLP);
    }
    public static void main(String[] args) throws Exception {
        TetrisBoardValueNeuralNetwork tbvnn = new TetrisBoardValueNeuralNetwork();
        tbvnn.loadModel("model.h5");
        TetrisBoard bd = new TetrisBoard(10, 24);
        bd.setCurrentPiece(new TetrisPiece(Piece.PieceType.LEFT_L).clockwisePiece());
        bd.setLowerLeftPos(new Point(0, 16));
        Move.executeDrop(bd);

        System.out.println(tbvnn.predict(new BoardState(bd).getInputs()));
    }

    @Override
    public double value(State state) {
        BoardState bs = (BoardState) state;
        return predict(bs.getInputs());
    }

}
