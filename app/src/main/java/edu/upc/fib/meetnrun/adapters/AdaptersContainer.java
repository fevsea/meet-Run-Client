package edu.upc.fib.meetnrun.adapters;


import edu.upc.fib.meetnrun.adapters.impls.ChallengeAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.ChatAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.FriendsAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.LoginAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.MeetingAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.RankingAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.UserAdapterImpl;
import edu.upc.fib.meetnrun.adapters.remote.ApiUtils;
import edu.upc.fib.meetnrun.adapters.remote.SOServices;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class AdaptersContainer {
    private IFriendsAdapter friendsAdapter;
    private IUserAdapter userAdapter;
    private IMeetingAdapter meetingAdapter;
    private ILoginAdapter loginAdapter;
    private IChatAdapter chatAdapter;
    private IChallengeAdapter challengesAdapter;
    private IRankingAdapter rankingsAdapter;
    private SOServices mServices;
    private static AdaptersContainer instance = null;

    private AdaptersContainer() {
        mServices = ApiUtils.getSOService();
    }

    public static AdaptersContainer getInstance() {
        if (instance == null) {
            instance = new AdaptersContainer();
        }
        return instance;
    }

    public IFriendsAdapter getFriendsAdapter() {
        if (friendsAdapter == null) {
            friendsAdapter = new FriendsAdapterImpl(mServices);
        }
        return friendsAdapter;
    }

    public void setFriendsAdapter(IFriendsAdapter friendsAdapter) {
        this.friendsAdapter = friendsAdapter;
    }

    public IUserAdapter getUserAdapter() {
        if (userAdapter == null) {
            userAdapter = new UserAdapterImpl(mServices);
        }
        return userAdapter;
    }

    public void setUserAdapter(IUserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    public IMeetingAdapter getMeetingAdapter() {
        if (meetingAdapter == null) {
            meetingAdapter = new MeetingAdapterImpl(mServices);
        }
        return meetingAdapter;
    }

    public void setMeetingAdapter(IMeetingAdapter meetingAdapter) {
        this.meetingAdapter = meetingAdapter;
    }

    public ILoginAdapter getLoginAdapter() {
        if (loginAdapter == null) {
            loginAdapter = new LoginAdapterImpl(mServices);
        }
        return loginAdapter;
    }

    public void setLoginAdapter(ILoginAdapter loginAdapter) {
        this.loginAdapter = loginAdapter;
    }


    public IChallengeAdapter getChallengesAdapter() {
        if (challengesAdapter == null) {
            challengesAdapter = new ChallengeAdapterImpl(mServices);
        }
        return challengesAdapter;
    }

    public void setChallengesAdapter(IChallengeAdapter challengesAdapter) {
        this.challengesAdapter = challengesAdapter;
    }

    public static void setInstance(AdaptersContainer instance) {
        AdaptersContainer.instance = instance;
    }

    public IChatAdapter getChatAdapter() {
        if (chatAdapter == null) {
            chatAdapter = new ChatAdapterImpl(mServices);
        }
        return chatAdapter;
    }

    public IRankingAdapter getRankingsAdapter(){
        if(rankingsAdapter == null){
            rankingsAdapter = new RankingAdapterImpl(mServices);
        }
        return rankingsAdapter;
    }

    public void setChatAdapter(IChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    public SOServices getmServices() {
        return mServices;
    }

    public void setmServices(SOServices mServices) {
        this.mServices = mServices;
    }
}
