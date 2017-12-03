package edu.upc.fib.meetnrun.adapters;

import edu.upc.fib.meetnrun.adapters.impls.ChallengeAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.FriendsAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.LoginAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.MeetingAdapterImpl;
import edu.upc.fib.meetnrun.adapters.impls.UserAdapterImpl;
import edu.upc.fib.meetnrun.remote.ApiUtils;
import edu.upc.fib.meetnrun.remote.SOServices;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class AdaptersContainer {
    private IFriendsAdapter friendsAdapter;
    private IUserAdapter userAdapter;
    private IMeetingAdapter meetingAdapter;
    private ILoginAdapter loginAdapter;
    private IChallengeAdapter challengesAdapter;
    private SOServices mServices;
    private static AdaptersContainer instance = null;

    private AdaptersContainer() {
        mServices = ApiUtils.getSOService();
        userAdapter = new UserAdapterImpl(mServices);
        friendsAdapter = new FriendsAdapterImpl(mServices);
        meetingAdapter = new MeetingAdapterImpl(mServices);
        loginAdapter = new LoginAdapterImpl(mServices);
        challengesAdapter = new ChallengeAdapterImpl(mServices);
    }

    public static AdaptersContainer getInstance() {
        if (instance == null) {
            instance = new AdaptersContainer();
        }
        return instance;
    }


    public IFriendsAdapter getFriendsAdapter() {
        return friendsAdapter;
    }

    public void setFriendsAdapter(IFriendsAdapter friendsAdapter) {
        this.friendsAdapter = friendsAdapter;
    }

    public IUserAdapter getUserAdapter() {
        return userAdapter;
    }

    public void setUserAdapter(IUserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    public IMeetingAdapter getMeetingAdapter() {
        return meetingAdapter;
    }

    public void setMeetingAdapter(IMeetingAdapter meetingAdapter) {
        this.meetingAdapter = meetingAdapter;
    }

    public ILoginAdapter getLoginAdapter() {
        return loginAdapter;
    }

    public void setLoginAdapter(ILoginAdapter loginAdapter) {
        this.loginAdapter = loginAdapter;
    }

    public SOServices getmServices() {
        return mServices;
    }

    public void setmServices(SOServices mServices) {
        this.mServices = mServices;
    }

    public IChallengeAdapter getChallengesAdapter() {
        return challengesAdapter;
    }

    public void setChallengesAdapter(IChallengeAdapter challengesAdapter) {
        this.challengesAdapter = challengesAdapter;
    }

    public static void setInstance(AdaptersContainer instance) {
        AdaptersContainer.instance = instance;
    }
}
