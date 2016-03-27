package morningsignout.phq9transcendi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import morningsignout.phq9transcendi.R;

public class QuizActivity extends AppCompatActivity {

    private TextView question, subtitle; //The text of the question
    private Button answer1; //Not at all || yes
    private Button answer2; //Few days a week || no
    private Button answer3; //More than half the week
    private Button answer4; //Everyday

    private int totalScore; //Used for answering questions
    private int scoreA;
    private int scoreB;
    private boolean redFlag; //If a red flag question gets answered
    private boolean redFlagQ; //If a question is a red flag question
    private boolean quizDone; //If all questions are answered
    private int questionNumber; //which question the user is on
    private boolean toggle; //For what section a question belongs in

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Grab and set content; inital setup
        question = (TextView) findViewById(R.id.questionView);
        subtitle = (TextView) findViewById(R.id.additionalText);
        answer1 = (Button) findViewById(R.id.answer1);
        answer2 = (Button) findViewById(R.id.answer2);
        answer3 = (Button) findViewById(R.id.answer3);
        answer4 = (Button) findViewById(R.id.answer4);

        reset();

        //Everything is setup, start quiz
        startQuiz();

        //Fab stuff that's automatically included with a fresh activity? commented out
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void reset() {
        totalScore = 0;
        scoreA = 0;
        scoreB = 0;
        redFlag = false;
        redFlagQ = false;
        quizDone = false;
        questionNumber = 1;
        toggle = true;
        subtitle.setText("");
        answer1.setText("Not at all");
        answer2.setText("Few days a week");
        answer3.setText("More than half the week");
        answer4.setText("Everyday");
    }

    private void startQuiz() {
        if(!quizDone) {
            updateQuestions();
            if(toggle) {
                if(!redFlagQ) {
                    toggleQuestionsA();
                } else {
                    toggleFlagQuestions();
                }
            } else {
                toggleQuestionsB();
            }
            questionNumber++;
            if(questionNumber > 20) {
                quizDone = true;
            }

        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        if(redFlag) {
            //alert
            question.setText("Your score is " + totalScore +", but one or more of your answers show that you may suffer from severe depression.");
        } else {
            //proceed normally with score
            question.setText("You're all done! Your score is " + totalScore);
        }
        if(totalScore == 0) {
            subtitle.setText("0: Zero depression. You show none of the signs of depression.");
        } else if(totalScore >= 1 && totalScore < 5) {
            subtitle.setText("1-4: Minimal depression. You have very few signs of depression.");
        } else if(totalScore >= 5 && totalScore < 10) {
            subtitle.setText("5-9: Mild depression. You might be suffering from mild depression.");
        } else if(totalScore >= 10 && totalScore < 15) {
            subtitle.setText("10-14: Moderate depression. You might be suffering from moderate depression.");
        } else if(totalScore >= 15 && totalScore < 20) {
            subtitle.setText("15-19: You might be suffering from moderately severe depression.");
        } else if(totalScore >= 20) {
            subtitle.setText("20+: You might be suffering from severe depression.");
        }
        answer1.setVisibility(View.VISIBLE);
        answer2.setVisibility(View.VISIBLE);
        answer3.setVisibility(View.VISIBLE);
        answer4.setVisibility(View.VISIBLE);

        answer1.setText("Take quiz again");
        answer2.setText("Go back");
        answer3.setText("References");
        answer4.setText("Resources");

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                startQuiz();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(QuizActivity.this, IndexActivity.class);
                startActivity(c);
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(QuizActivity.this, ResourceActivity.class);
                a.putExtra("page_type", "Resources");
                startActivity(a);
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(QuizActivity.this, ResourceActivity.class);
                b.putExtra("page_type", "References");
                startActivity(b);
            }
        });
    }

    private void toggleQuestionsA() {

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA = 0;
                startQuiz();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA = 1;
                startQuiz();
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA = 2;
                startQuiz();
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA = 3;
                startQuiz();
            }
        });
    }

    private void toggleQuestionsB() {

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB = 0;
                calculateScore();
                startQuiz();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB = 1;
                calculateScore();
                startQuiz();
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB = 2;
                calculateScore();
                startQuiz();
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB = 3;
                calculateScore();
                startQuiz();
            }
        });
    }

    private void toggleFlagQuestions() {
        if(questionNumber == 18) {
            answer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startQuiz();
                }
            });

            answer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startQuiz();
                }
            });

            answer3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startQuiz();
                }
            });

            answer4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redFlag = true;
                    startQuiz();
                }
            });
        } else {

            answer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redFlag = true;
                    startQuiz();
                }
            });

            answer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startQuiz();
                }
            });
        }
    }

    private void calculateScore() {
        if(scoreA >= scoreB) {
            totalScore += scoreA;
        } else {
            totalScore += scoreB;
        }
    }

    private void updateQuestions() {
        switch(questionNumber) {
            //SECTION 1
            case 1:
                question.setText("Do you find that you’ve lost a lot of interest in things used to be interested in? This can include hobbies, friends, work, food, or sex");
                toggle = false;
                break;
            case 2:
                question.setText("Do you find that a lot of things are no longer enjoyable to you that you previously found enjoyable, or that you simply don’t go out and try to do enjoyable things anymore?");
                toggle = true;
                break;
            //SECTION 2
            case 3:
                question.setText("Do you ever feelings of being down or or depressed?");
                toggle = false;
                break;
            case 4:
                question.setText("Do you ever have feelings of hopelessness?");
                break;
            //SECTION 3
            case 5:
                question.setText("Sleep is an incredibly important part of both maintaining and can also shed light on how our brains are functioning. Have you had problems with falling asleep, staying asleep, or just end up sleeping a lot less in the last two months?");
                toggle = false;
                break;
            case 6:
                question.setText("How about on the other hand? Many depressed people often have the feeling of lethargy and sleeping much more than they normally do.");
                break;
            //SECTION 4
            case 7:
                question.setText("How has your energy level been? Do you feel tired? Drained? Feel like there is no energy to do anything?");
                toggle = true;
                break;
            //SECTION 5
            case 8:
                question.setText("Someone’s appetite can be a very good indicator of when someone’s getting depressed. Have you noticed yourself eating less than you normally do?");
                toggle = false;
                break;
            case 9:
                question.setText("Okay, how about a lot more than you normally do? Have you been eating lots of food at once?");
                break;
            //SECTION 6
            case 10:
                question.setText("A lot of people with depression have negative thoughts that are hard to control that creep in. have you had thoughts that you’re a failure?");
                toggle = true;
                break;
            //SECTION 7
            case 11:
                question.setText("How’s your concentration been? Lots of people with depression have trouble concentrating on reading books or doing their homework. Have you had trouble with concentrating lately?");
                toggle = false;
                break;
            case 12:
                question.setText("Have you noticed it becoming harder to finish something you started?");
                toggle = true;
                break;
            //SECTION 8
            case 13:
                question.setText("Some people with depression actually feel a sense of physical slowing. have you felt a sense of physical “slowness” about yourself, like moving slowly?");
                toggle = false;
                break;
            case 14:
                question.setText("Other people have said that they’ve been told they move or talk more slowly when they’re depressed. Have you had anyone comment on this lately?");
                toggle = true;
                break;
            //SECTION 9
            case 15:
                question.setText("This one is really important - do you ever thinking about hurting yourself in some way, or ending your life?");
                toggle = false;
                break;
            case 16:
                question.setText("How about the thought of thinking that you’d be better off dead, or that it would be great to just fall asleep and never wake up?");
                toggle = true;
                break;
            //RED FLAG QUESTIONS
            case 17:
                question.setText("In the past year have you felt depressed or sad most days even if you felt okay sometimes?");
                redFlagQ = true;
                toggle = true;
                answer1.setText("Yes");
                answer2.setText("No");
                answer3.setVisibility(View.INVISIBLE);
                answer4.setVisibility(View.INVISIBLE);
                break;
            case 18:
                question.setText("If you are experiencing any of the problems that we asked you about, how much do these problems problems keep you from being 100% at your work, study, go about your daily business, get along with other people, and maintain your relationships?");
                toggle = true;
                answer1.setText("Doesn't affect me");
                answer2.setText("A little");
                answer3.setText("Somewhat");
                answer4.setText("Crippling");
                answer3.setVisibility(View.VISIBLE);
                answer4.setVisibility(View.VISIBLE);
                break;
            case 19:
                question.setText("Has there been a time in the past month when you have had serious thoughts about ending your life?");
                toggle = true;
                answer1.setText("Yes");
                answer2.setText("No");
                answer3.setVisibility(View.INVISIBLE);
                answer4.setVisibility(View.INVISIBLE);
                break;
            case 20:
                question.setText("Have you ever in your whole life try to kill yourself or make a suicide attempt?");
                toggle = true;
                answer1.setText("Yes");
                answer2.setText("No");
                answer3.setVisibility(View.INVISIBLE);
                answer4.setVisibility(View.INVISIBLE);
                break;
            //default
            default:
                question.setText("Ipsum Lorem");
                break;
        }
    }



}