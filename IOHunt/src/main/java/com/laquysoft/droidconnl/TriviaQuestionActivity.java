/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laquysoft.droidconnl;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TriviaQuestionActivity extends BaseActivity implements
OnTouchListener {

	private String ID_LEADERBOARD;

	public TriviaQuestionActivity() {
		super(CLIENT_PLUS | CLIENT_GAMES);
	}

	LinearLayout linearLayout;
	LinearLayout textViewLayout;

	Handler h = new Handler();
	private boolean keepAnimating = false;
	private boolean hasAnswered = false;

	AnimationDrawable nononoAnimation;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.activity_trivia_question);
		Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

		ID_LEADERBOARD = getResources().getString(R.string.leaderboard_id);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayoutTQ);

		Clue clue = hunt.getLastCompletedClue();

		hunt.setQuestionState(Hunt.QUESTION_STATE_QUESTIONING);
		hunt.save(getResources(), getApplicationContext());

		if ( clue != null)
		{
			if (clue.question != null) {
				setQuestion(clue.question);
			} else {
				// Something is pretty wrong, so let's get out of here.
				finish();
			}
		}else {
			// Something is pretty wrong, so let's get out of here.
			finish();
		}


	}

	public void setQuestion(TriviaQuestion tq) {
		LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
				R.layout.question_frag, null);

		TextView textView = (TextView) ll.findViewById(R.id.question_text_frag);

		textView.setText(tq.question);
		textView.setId(-1);
		linearLayout.addView(ll);
		textView.setVisibility(View.VISIBLE);

		for (int i = 0; i < tq.answers.size(); i++) {
			LinearLayout ll2 = (LinearLayout) getLayoutInflater().inflate(
					R.layout.answer_frag, linearLayout);
			AnswerTextView atv = (AnswerTextView) ll2
					.findViewById(R.id.answer_text_frag);
			atv.setText(tq.answers.get(i));
			atv.answerNum = i;
			atv.setId(-1);
			atv.setOnTouchListener(this);
		}

		linearLayout.forceLayout();
	}

	@Override
	public void onResume() {
		super.onResume();
		setAnimating(true);
	}

	@Override
	public void onStop() {
		super.onStop();
		setAnimating(false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final Clue clue = Hunt.getHunt(getResources(), getApplicationContext())
				.getLastCompletedClue();

		// Wait if we're waiting for toast pop.
		if (hasAnswered) {
			return true;
		}

		AnswerTextView tv = (AnswerTextView) v;
		// Deal with flipping colors on and off
		tv.onTouch(tv, event);

		// Don't do anything on down besides flip color.
		if (event.getAction() != MotionEvent.ACTION_UP) {
			return true;
		}

		final Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

		hunt.setQuestionState(Hunt.QUESTION_STATE_NONE);
		hunt.save(getResources(), getApplicationContext());

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};

        ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.AppTheme );

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

		boolean isUnderPar = hunt.getSecondsLeft() > 0;

		hasAnswered = true;
		keepAnimating = false;

		if (tv.answerNum == clue.question.correctAnswer) {

			builder.setMessage(clue.question.rightMessage)
			.setPositiveButton("OK", dialogClickListener).setCancelable(false).show();
			hunt.achievementManager.storeIncrement(AchievementManager.ID_5_TRIVIA_CORRECTLY);
			if (isUnderPar) {
				hunt.achievementManager.storeIncrement(AchievementManager.ID_TEACHERS_PET);
                hunt.achievementManager.onQuestionAnswered(getApiClient(), isUnderPar, this);
			}
			hunt.save(getResources(), getApplicationContext());


		} else {


			/*AlertDialog.Builder alertadd = new AlertDialog.Builder(
					this);
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.wronganswer, null);
			ImageView nononoImage = (ImageView) view.findViewById(R.id.nonono);
			nononoImage.setVisibility(View.VISIBLE);
			nononoAnimation.start();

			alertadd.setView(view);
			alertadd.setMessage(clue.question.wrongMessage);
			alertadd.setPositiveButton("OK", dialogClickListener).setCancelable(false).show();
			 */



			builder.setMessage(clue.question.wrongMessage)
			.setPositiveButton("OK", dialogClickListener).setCancelable(false).show();
		}

		return true;
	}

	public void setAnimating(Boolean val) {
		keepAnimating = val;
		if (val) {
			tick();
		}
	}

	private Runnable ticker = new Runnable() {
		@Override
		public void run() {
			tick();
		}
	};

	public void tick() {
		int timeLeft = Hunt.getHunt(getResources(), getApplicationContext())
				.getSecondsLeft();
		TextView tv = (TextView) findViewById(R.id.timeLeft);

		if (timeLeft <= 0) {
			tv.setText("Missed par");
		} else {
			tv.setText("Par time left:" + timeLeft);
		}

		if (keepAnimating) {
			h.postDelayed(ticker, 200);
		}
	}

    @Override
    protected int getLayoutResource() {
       return R.layout.activity_trivia_question;
    }



}
