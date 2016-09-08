package stepview.qwy.com.mystepview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import stepview.qwy.com.stepview.HorizontalStepView;

/**
 * 日期：16/7/26 15:06
 * <p>
 * 描述：
 */
public class TestHorizontalStepViewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_horizontal);
        findViewById(R.id.btn_status).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HorizontalStepView stepView = (HorizontalStepView) findViewById(R.id.stepview);
                List<String> list0 = new ArrayList<>();
                list0.add("接单");
                list0.add("打包");
                list0.add("出发");
                list0.add("送单");
                list0.add("完成");
                list0.add("支付");
                stepView.setStepsViewIndicatorComplectingPosition(2)//设置完成的步数
                        .setStepViewTexts(list0)//总步骤
                        .setTextSize(16)//set textSize
                        .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))//设置StepsViewIndicator完成线的颜色
                        .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                        .setStepViewComplectedTextColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))//设置StepsView text完成线的颜色
                        .setStepViewUnComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                        .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                        .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                        .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon
            }
        });


    }
}
