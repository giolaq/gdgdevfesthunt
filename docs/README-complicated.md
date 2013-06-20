
# The Full Google I/O Hunt>

We have also included code for the full Google I/O Hunt experience.
This includes three new features:

* Google Games signin
* Achievements
* Trivia Questions

This Android project is in <tt>IOHunt/</tt>.

<center>
<img src="images/achievements/icon_afoot.gif">
<img src="images/achievements/icon_found.gif">
<img src="images/achievements/icon_halfway.gif">
<img src="images/achievements/icon_met.gif">
<img src="images/achievements/icon_speedrun.gif">
<img src="images/achievements/icon_teacherspet.gif">
<img src="images/achievements/icon_trivia.gif">
</center>

<center>
<img src="images/trivia.png">
</center>

## Setup

To compile this, you need to set up a proper game project in the
Developer Console, as found on
[developers.google.com/games/services](https://developers.google.com/games/services).
In particular, you'll need to:

* [Setup your
  game](https://developers.google.com/games/services/android/quickstart),
  using <tt>IOHunt</tt> instead of <tt>TypeANumber</tt>.
* Make sure you have included the <tt>google-play-services_lib</tt> libproject as a dependency (**Properties -> Android**), as noted in the instructions above. (If you do not, <tt>GameHelper</tt> will be an undefined class.)
* Put your project ID in <tt>/res/values/ids.xml</tt>
* You'll need to create all the achievements and put their ID numbers
  in <tt>/res/values/ids.xml</tt>.
* Add your name to the testers list.

Once you've done that, you should be able to sign in with Google Games
and win achievements.  Note that you will need at least five clues to
win all the achievements, each with trivia questions.

## Trivia Questions

Each clue object has an optional <tt>question</tt> field that looks a
bit like this:

<pre><code>
      question: 
      {  "question": "What is the name of the T-Rex on Google's Main campus?",
         "answers": ["Larry", "Joe", "Stan"],
         "correctAnswer": 2,
         "wrongMessage": "No, it's Stan, just outside B44.\n",         
         "rightMessage": "That's right!  It's Stan, who is sometimes covered in flamingos."
      }
</code></pre>

The values should be self-explanatory.

You need at least 5 trivia questions to set off all the achievements.
Trivia questions are attached directly to the clue object as shown in <tt>samplehunt.json.</tt>

### Flow control

Having many different activities creates flow control challenges, as
each time an activity starts it needs to connect to Google Play
Services to stay signed in.

A better approach, one we will likely take in future revisions, is to
use fragments and and keep a signed-in fragment on every screen.

