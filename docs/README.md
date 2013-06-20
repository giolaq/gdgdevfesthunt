
[Here is the source](http://code.google.com/p/google-io-hunt) from Google I/O Hunt 2013, as played by attendees
of Google I/O in Moscone Center.

<center><img src="images/landing.png"><img src="images/androidhuntstarter.png"></center>


Players came to the G+ desk, scanned three tags with their NFC-enabled
Android devices, and were off on a hunt for clues through Moscone
Center before finding Alex, the hidden dog!

<center><img src="images/winner.png"><img src="images/firstclue.png"></center>

This guide will help you set up your own hunt with your own tags and
data anywhere in the world.

Good luck!

## What is a hunt?

Google I/O Hunt is a treasure hunt (not, technically, a scavenger
hunt, since there is an order to the clues).  We have provided a
sample data set that has only two clues, but you can expand that to
any number of locations.

The hunt is made up of clues and tags.

* Clues are locations in your hunt, and have a picture and hint text.
  Clues are shuffled by their difficulty.  You will do all the clues
  of one difficulty before proceeding to the next one.

* Tags are the hidden NFC tags you scan at eatch location.

When you reach the end, you get a prize!

## Physical Materials

To build a hunt, you will need NFC tags and one or more NFC-enabled
device running Android 4.0 (Ice Cream Sandwich) or better.  We could suggest
[Nexus
device](https://play-next-dogfood.corp.google.com/store/devices/details?id=nexus_7_16gb),
but nearly anything with an NFC scanner will work.

You will need to turn on NFC in **Settings -> Wireless &
Networking -> More**.  Also, turn off Airplane Mode!

### Getting Tags

For Google I/O, we had custom laser-cut tags from
[Kovio](http://kovio.com) that were printed with our logo.  If you
just need a few tags, another place to go is
[Tagstand](http://tagstand.com) which sells kits or a la carte.

<center><img src="images/customtag-pic.png"><img src="images/allweather.png"></center>

The big tags we had for I/O had a large scannable area.  The small
all-weather ones to the right work well, but sometimes it takes a
second to find the NFC scanner in your phone.

One thing to think about is metal isolation.  If you need to attach
your tags to metal objects, you should get isolated tags so there
isn't to much "echo" when you're scanning.  I've found even isolated
tags can be unreliable when attached to a large steel girder.

## Android Projects

You need two Android projects, which you will need to compile from
source.  The first is <tt>SimpleIOHunt/</tt> which contains a
simplified version of the game, and <tt>HuntWriter</tt>.

Import these into your Android IDE as you would normally.  In Eclipse,
this would be **File -> Import -> Android -> Existing Android Code
into Workspace** and choose the right directories.

These should compile correctly out of the box without any further
dependencies.

If you wish to modify the story at the beginning and/or the end, edit
the <tt>/res/values/strings-ah.xml</tt> and change the appropriate
strings or image resources.

<i>If you want to use the complete code of Google I/O Hunt, check out
<tt>IOHunt/</tt>, and set it up using [these instructions](README-complicated.html).</tt></i>

## Hunt data

The hunt data in this version of Android Hunt is stored in a single
.zip file for convenience in <tt>/res/raw/hunt.zip</tt>.  The unzipped
data can be found in <tt>huntdata/</tt>. 

### Making a new hunt

To make your own hunt data:

* Edit the <tt>.json</tt> file to add your new clues and tags
* Add the right of <tt>.jpgs</tt> that represent your clue. (Keep 'em small!)
* Create the compressed <tt>hunt.zip</tt>.
   * If you have a Unix/Linux/Mac, run <tt>makeBundle.sh</tt>, which creates hunt.zip and copies it
     into the right places.
   * Otherwse, if you are on any other platform, zip the huntdata
     directory and copy it into the <tt>/res/raw/</tt> directories for
     <tt>SimpleIOHunt</tt> and <tt>HuntWriter</tt>.

Remember to refresh your resources and build clean if you are using an
IDE like Eclipse.

Now run, and your data should be there!

### The Hunt object

This is the shape of a Hunt in .json:

<pre><code>
{
  type: "hunt",  # Not really used, but nice for your editor
  displayName: "Test Hunt", # Not used
  id: "1", 
  clues: [  # list of clues 
  ]
}
</code></pre>

Most of these fields make this easier to read in a proper <tt>.json</tt>
editor, but you don't need to change them if you don't want to.

To make your hunt, just fill in the clues.

### Writing Clues

Clues are data structures that contain:

  * id: string, internal reference
  * display image: string, the filename of picture you want to display
  * displayName: string, the title in the title bar
  * displayText: string, the text under the picture
  * tags: string, a list of tags
  * shuffle group: integer, The difficulty level, starting with 0.

To set this up, you need to take a picture of the stove, and then create
a clue structure in the <tt>samplehunt.json</tt> data and add the .jpg
to the app.

Here's the .json snippet for that clue:

<pre><code>
    {
      type: "clue",
      id: "Stove",
      shufflegroup: 1,  
      displayName: "Getting warmer!",
      displayText: "Find this item and scan any tags you find there!",
      displayImage: "stove.jpg", # Corresponds to image file; png/jpg accepted
      tags: [        
        {id: "stove-0"},
        {id: "stove-1"},
        {id: "stove-2"}
      ],
    }
</code></pre>

#### Clue order and difficulty

Clues are shown in <tt>shufflegroup</tt> order.  So, all the clues in
shufflegroup 0 are shown first, then all the clues in shufflegroup 1,
etc.  The clues are shuffled once per phone, so you will always get
the same shuffle, but different people with different phones would get
different orders.

### Tags

Each tag in Android Hunt contains unique data, so for a 40-tag hunt
you will have to have 40 different tags.

#### Tag format

Tags are uris.  For this the tag 'stove-0' it would look like this:

    https://nfchunt.appspot.com/f?c=stove-0

We are using http links because they are small and will fit on
hobbyist tags (which can be as small as 140 characters).

There are other kinds of data you can store, including external
records just for your app which are harder to fake.  However, links
are very convenient, and unless you expect a lot of cheating (an NFC
hunt only for NFC enthusiast hackers?) you should be OK.

#### Decoy Tags

If you want to surprise your users, you can have a decoy tag, which is
just a tag with this URL:

    https://nfchunt.appspot.com/f?c=decoy

For obvious reasons, don't name an normal tag with the id "decoy".

Scanning a decoy brings up Villain Android.


#### Writing tags

You can write tags with any of a number of free tag writers (you juse
need a URI), but to make it easier, we provide the HuntWriter app in
source code form.  You can put your release <tt>hunt.zip</tt> file in
the <tt>res/raw</tt> directory, and it will unpack it so you can write
your whole hunt at once.

<center><img src="images/huntwriter.png"></center>

Check which tag you want to write, and tap a blank or rewritable tag.
If you get an error, inspect the tag with a tag info app, like
[Taginfo](https://play.google.com/store/apps/details?id=com.nxp.taginfolite&hl=en)
and see if the tag is actually empty and/or set in write-only mode.

### When things go wrong

The most likely problem you will have is an invalid .json file.  You
can look for the error in Hunt.java (it's a catch with a stacktrace
print).  This will sometimes manifest itself at runtime to show the
wrong number of clues or no clues at all, at which point you will jump
to the victory screen.

You can forestall some of this by validating your .json in a [json
validator](http://jsonformatter.curiousconcept.com/) before zipping
and copying into your project.

If you get a dog icon instead of your expected picture, you've got an
invalid image value.

## Hunt Design

Designing hunts is very straightforward.  You need places and tag
counts at each place.  Here are some notes from when we did it:

* Walk the area where the hunt will take place and take pictures of
  everything that looks like a good location.  You won't use all the
  locations, and sometimes the pictures don't look great.  Shoot in
  landscape for best effect!
* Our general "fairness" rules are that players don't have to open anything
  to find tags---everything is in plain sight, as long as you're
  standing in the right place.
* We used three tags where we could.  We have found one easy-to-spot one and two
  hard-to-find ones are fun.  They easy one reassures people they are
  in the right place, and then the hard ones make them do a little work.
* You can use height to effect---hide some near the floor, some up high.  (See [Spock's
  advice.](http://www.youtube.com/watch?v=iPQfwmfRq2s).)
* Test your hunt before you launch.  Find some people to test it for you.
* Reuntuning after you launch is
  easy; you can always move your tags around a little!  We did so at
  I/O to manage traffic flow.
* An easy location has clearly-identified landmarks in the photo.  A
  hard clue might be of something where there are lots of them (like a
  streetlamp) or a close zoom of something (such as part of a logo).
* Don't make your last clue the hardest---your hardest should be one or
  two locations before the end, so people have an acceleration toward
  victory!
* <b>Keep your jpgs small.</b>  It'll keep your .apk slim and startup short.  On most mobile
  devices a 650x400 jpg with high compression is sufficient.

## Tell us more!

There are a lot of directions you could take Google I/O Hunt, and we
would love to hear from you!  Please send us email at wolff@google.com
if you have anything to tell us, or share it with us on [G+](http://plus.google.com/+WolffDobson/)!
