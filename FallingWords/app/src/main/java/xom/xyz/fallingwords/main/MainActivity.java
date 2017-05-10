package xom.xyz.fallingwords.main;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xom.xyz.fallingwords.R;

public class MainActivity extends AppCompatActivity implements  IMainView   {
    MainPresenterImpl  mainPresenter;
    SimpleArcDialog hud;

    @BindView(R.id.tv_wrong_score)
    TextView mTVWrongScore;

    @BindView(R.id.tv_right_score)
    TextView mTVRightScore;

    @BindView(R.id.tv_question)
    TextView mTVQuestion;

    @BindView(R.id.tv_answer)
    TextView mTVAnswer;

    @BindView(R.id.btn_start_game)
    Button btnStartNewGame;


    @BindView(R.id.pb_timer_circle)
    ProgressBar pbTimer;
    @BindView(R.id.tv_counter)
    TextView mTVCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainPresenter = new MainPresenterImpl(this,new MainInteractorImpl());
        mainPresenter.onLoadData();
    }

    @Override
    public void showLoading() {
        if (hud == null) {
            hud = new SimpleArcDialog(this);
            ArcConfiguration configuration = new ArcConfiguration(this);
            configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
            int[] colors = {ContextCompat.getColor(this, R.color.colorAccent),
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)};
            configuration.setColors(colors);
            hud.setConfiguration(configuration);
        }
        hud.show();
    }

    @Override
    public void hideLoading() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    @Override
    public void showStartNewGameButton() {
        btnStartNewGame.setVisibility(View.VISIBLE);
    }

    @Override
    public void startGame(String question,String proposedAnswer,Long duration){
        btnStartNewGame.setVisibility(View.INVISIBLE);

        mTVAnswer.setText(proposedAnswer);
        mTVQuestion.setText(question);

        //Start Counter
        startCounter(duration);



    }

    @Override
    public void setRightScore(String scrore) {
        mTVRightScore.setText(scrore);
    }

    @Override
    public void setWrongScore(String scrore) {
        mTVWrongScore.setText(scrore);
    }

    @Override
    public void showErrorMessage(String string) {
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btn_correct_translation) void onRightBtnClicked(){
        mainPresenter.onCorrectTranslationButtonClicked();
    }

    @OnClick(R.id.btn_wrong_translation)void onWrongTranslationButtonClicked(){
        mainPresenter.onWrongTranslationButtonClicked();
    }

    @OnClick(R.id.btn_start_game)void onStartNewGameBtnClicked(){
        mainPresenter.onStartNewGameClicked();
    }

    private CountDownTimer mProgressCounter;

    private void startCounter(final long duration){
        pbTimer.setProgress(0);

        mProgressCounter = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                String v = String.format(Locale.US, "%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                mTVCounter.setText(String.format(Locale.US, v + ":" + "%02d", va));
                int progress = (int) (duration  - millisUntilFinished);
                progress = progress * 100 / (int)duration ;
                pbTimer.setProgress(progress);
                //Animate Answer To Bottom
            }

            public void onFinish() {
                mTVCounter.setText("00:00");
                pbTimer.setProgress(100);
            }
        };
        mProgressCounter.start();
    }

}
