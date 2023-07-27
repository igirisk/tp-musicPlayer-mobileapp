package sg.edu.tp.musicstream;
public class SongCollection {

    public static Playlist playlist = new Playlist();

    static {
        Song theWayYouLookTonight = new Song("S1001",
                "The Way You Look Tonight",
                "Michael Buble",
                "https://p.scdn.co/mp3-preview/a5b8972e764025020625bbf9c1c2bbb06e394a60?cid=2afe87a64b0042dabf51f37318616965",
                4.66,
                R.drawable.michael_buble_collection);


        //1. Create another Song object below
        Song billieJean = new Song("S1002",
                "Billie Jean",
                "Michael Jackson",
                "https://p.scdn.co/mp3-preview/71638a1eac196a5daa9fbf152693585e323d8558?cid=2afe87a64b0042dabf51f37318616965",
                4.9,
                R.drawable.billie_jean);

        Song voicesOfTheChord = new Song("S1003",
                "Voices of the Chord",
                "Hiroyuki Sawano",
                "https://p.scdn.co/mp3-preview/66d769c60ea587c7ecf2cfc4ad0ae66d8de1677a?cid=2afe87a64b0042dabf51f37318616965",
                3.04,
                R.drawable.voices_of_the_chord);

        Song theRumbling = new Song("S1004",
                "The Rumbling",
                "SiM",
                "https://p.scdn.co/mp3-preview/4b5fc5fb8424f4197f1abcb85edd04a058dc6cb0?cid=2afe87a64b0042dabf51f37318616965",
                3.41,
                R.drawable.the_rumbling);

        Song YouSeeBIGGIRL = new Song("S1005",
                "YouSeeBIGGIRL/T:T",
                "Hiroyuki Sawano",
                "https://p.scdn.co/mp3-preview/9a6402fe05c56670cf6dc9833218edbc8a2687cd?cid=2afe87a64b0042dabf51f37318616965",
                5.99,
                R.drawable.youseebiggirl);


        playlist.add(theWayYouLookTonight);
        playlist.add(billieJean);
        playlist.add(voicesOfTheChord);
        playlist.add(theRumbling);
        playlist.add(YouSeeBIGGIRL);

    }
}