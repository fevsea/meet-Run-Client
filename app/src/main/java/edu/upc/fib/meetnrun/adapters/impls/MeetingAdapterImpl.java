package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.Utils.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class MeetingAdapterImpl implements IMeetingAdapter {
    private SOServices mServices;

    public MeetingAdapterImpl(SOServices soServices) {
        mServices = soServices;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        List<Meeting> l = new ArrayList<>();
        try {
            Response<MeetingServer[]> res = mServices.getMeetings().execute();
            MeetingServer[] array = res.body();
            for (int i = 0; i < array.length; i++) {
                l.add(array[i].toGenericModel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException, AutorizationException {
        Forms.CreateMeeting ur = new Forms.CreateMeeting(title, description, _public, level, date, latitude, longitude);
        UserServer u = null;
        MeetingServer m = null;
        try {
            Response<MeetingServer> ret = mServices.createMeeting(ur).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof ParamsException) {
                throw (ParamsException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return m.toGenericModel();
    }

    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        MeetingServer m = null;
        try {
            Response<MeetingServer> ret = mServices.getMeeting(id).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) throw (NotFoundException) e;
        }
        return m.toGenericModel();
    }

    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException, AutorizationException {
        boolean ok = false;
        MeetingServer ms = new MeetingServer(obj);
        try {
            Response<Void> ret = mServices.updateMeeting(obj.getId(), ms).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            } else {
                ok = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }


    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException, AutorizationException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.deletetMeeting(id).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public List<User> getParticipantsFromMeeting(int meetingId) throws AutorizationException, ParamsException {
        List<User> ul = new ArrayList<>();
        try {
            Response<List<UserServer>> ret = mServices.getAllParticipantsFromMeeting(meetingId).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<UserServer> u = ret.body();

            for (int i = 0; i < u.size(); i++) {
                ul.add(u.get(i).toGenericModel());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            }
        }
        return ul;
    }

    @Override
    public boolean joinMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.joinMeeting(meetingId).execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException)
                throw (AutorizationException) e;
        }
        return ok;
    }

    @Override
    public boolean leaveMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.leaveMeeting(meetingId).execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException)
                throw (AutorizationException) e;
        }
        return ok;
    }

    @Override
    public List<Meeting> getAllMeetingsFilteredByName(String query) {
        List<Meeting> l = new ArrayList<>();
        try {
            Response<MeetingServer[]> res = mServices.getAllMeetingsFiltered(query).execute();
            MeetingServer[] array = res.body();
            for (int i = 0; i < array.length; i++) {
                l.add(array[i].toGenericModel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }
}
