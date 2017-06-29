package userinformation;

import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Music {
/////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////单数据
//	public String fsong ; 
//	public String fsinger;
//	public String f;
//	
//	public Music() {
//		// TODO Auto-generated constructor stub
//	}
//	
//	public Music(String fsong, String fsinger, String f) {
//		this.fsong = fsong;
//		this.fsinger = fsinger;
//		this.f = f;
//	}
//
//	@Override
//	public String toString()
//	{
//		return "User{" +
//				"username='" + fsong + '\'' +
//				", password='" + fsinger + '\'' +
//				'}';
//	}
//////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////数组 
	@SerializedName("list")
    private List<song> list;

    public List<song> getSong() {
        return list;
    }

    public void setSong(List<song> list) {
        this.list = list;
    }
	public song Music123()
	{
		song temp = null;
	    for(int i = 0 ;  i <list.size(); i++ ){
             temp = list.get(0);
	    }
	    //String a= tempObj[0];
		//String[] toBeStored = list.toArray(new String[list.size()]);
		//String a= Arrays.toString(toBeStored);
		return temp;
	}

    public static class song {
        @SerializedName("fsong")
        private String fsong="100";
        @SerializedName("fsinger")
        private String fsinger;
        @SerializedName("f")
        private String f;
        song(String a,String b,String c){
        	this.fsong=a;
        	this.fsinger=b;
        	this.f=c;       	
        }

        public String getSongname() {
            return fsong;
        }

        public void setSongname(String fsong) {
            this.fsong = fsong;
        }

        public String getSingername() {
            return fsinger;
        }

        public void setSingername(String fsinger) {
            this.fsinger = fsinger;
        }

        public String getSongid() {
            //return f.substring(0,7);//f前七位为ID
        	return f.substring(0, f.indexOf("|"));
        }

        public void setSongid(String f) {
            this.f = f;
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////
//    @SerializedName("song")
//    private List<Musics> song;
//
//    public List<Musics> getSong() {
//        return song;
//    }
//
//    public void setSong(List<Musics> song) {
//        this.song = song;
//    }
//
//    public static class Musics {
//        ///////////////////////////////////////////////////////
//        @SerializedName("list")
//        private String songlist;
//
//        public String getSonglist() {
//            return songlist;
//        }
//
//        public void setSong(String songlist) {
//            this.songlist = songlist;
//        }
//        public static class Musicslist {
//            @SerializedName("fsong")
//            private String fsong;
//            @SerializedName("fsinger")
//            private String fsinger;
//            @SerializedName("f")
//            private String f;  
//
//
//            public String getSongname() {
//                return fsong;
//            }
//
//            public void setSongname(String fsong) {
//                this.fsong = fsong;
//            }
//
//            public String getArtistname() {
//                return fsinger;
//            }
//
//            public void setArtistname(String fsinger) {
//                this.fsinger = fsinger;
//            }
//
//            public String getSongid() {
//                return f;
//            }
//
//            public void setSongid(String f) {
//                this.f = f;
//            }
//        }
        ////////////////////////////////////////////////////////

}