package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh Swami on 16-07-2016 for the project PersonalAssistant.
 */
public class MachineBudget {

    private static Context context;

    /**
     *
     * @param c Context
     */
    MachineBudget(Context c){
        this.context = c;
    }

    /**
     * @param features The budget values
     * @param values   The costs for the respective budgets
     * @param theta0   Coefficient of Math.pow(x,0)
     * @param theta1   Coefficient of x
     * @return cost
     */
    private double cost(List<Double> features, List<Double> values, double theta0, double theta1) {
        int m = values.size();
        List<Double> h_x = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            double predict = theta0 + features.get(i) * theta1;
            h_x.add(predict);
        }
        double sum = 0;
        for (int i = 0; i < m; ++i) {
            double square = Math.pow((h_x.get(i) - values.get(i)), 2);
            sum += square;
        }
        double cost = (1.0 / (2 * m)) * sum;
        return cost;
    }

    /**
     * @param features   Budgets
     * @param values     Costs
     * @param theta0     Coefficient of Math.pow(x,0)
     * @param theta1     Coefficient of x
     * @param alpha      The learning rate
     * @param iterations The number of iterations
     * @return theta
     */
    private List<Double> gradient_descent(List<Double> features, List<Double> values, double theta0, double theta1, double alpha, int iterations) {
        //Changed to include only the previous months values
        int m = values.size() -1;
        double sum0, sum1;
        List<Double> theta = new ArrayList<>();
        for (int k = 0; k < iterations; ++k) {
            sum0 = 0.0;
            sum1 = 0.0;
            for (int i = 0; i < m; ++i) {
                double predict = theta0 + features.get(i) * theta1;
                sum0 += (predict - values.get(i));
                sum1 += (sum0 * features.get(i));
            }
            theta0 = theta0 - alpha * (1.0 / m) * sum0;
            theta1 = theta1 - alpha * (1.0 / m) * sum1;
        }
        theta.add(theta0);
        theta.add(theta1);
        return theta;
    }

    /**
     *
     * @param b Budget file's name
     * @param c Cost files name
     * @param bud The budget to calculate cost for
     * @return cost
     */
    public double predictCost(String b,String c,double bud){
        double ret;
        int iters = 600000;
        double alpha = 0.0001,theta0 = 0.0,theta1 = 0.0;
        FileHelper fi = new FileHelper(context);
        List<Double> features = fi.read(b);
        //Toast.makeText(context,"Features is "+features.toString(),Toast.LENGTH_LONG).show();
        List<Double> values = fi.read(c);
        //Toast.makeText(context,"Values is "+values.toString(),Toast.LENGTH_LONG).show();
        List<Double> theta = gradient_descent(features,values,theta0,theta1,alpha,iters);
        //Toast.makeText(context,"Theta is "+theta.toString(),Toast.LENGTH_LONG).show();
        ret = theta.get(0) + theta.get(1)*bud;
        return ret;
    }
}
