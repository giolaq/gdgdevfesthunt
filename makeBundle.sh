rm -f hunt.zip

zip hunt.zip huntdata/*
cp hunt.zip IOHunt/res/raw/
cp hunt.zip HuntWriter/res/raw/
cp hunt.zip SimpleIOHunt/res/raw/

echo After running this, do not forget to refresh your resources.
