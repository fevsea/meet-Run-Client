package edu.upc.fib.meetnrun.adapters.impls;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.TrackServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.TrackingData;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.Utils.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.Utils.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class MeetingAdapterImpl implements IMeetingAdapter {
    private final SOServices mServices;

    public MeetingAdapterImpl(SOServices soServices) {
        mServices = soServices;
    }

    @Override
    public List<Meeting> getAllMeetings(int page) {
        List<Meeting> l = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<MeetingServer>> res =
                    mServices.getAllMeetings(SOServices.PAGELIMIT, offset).execute();
            PageServer<MeetingServer> psm = res.body();
            for (int i = 0; i < psm.getResults().size(); i++) {
                l.add(psm.getResults().get(i).toGenericModel());
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
    public List<User> getParticipantsFromMeeting(int meetingId, int page) throws AutorizationException, ParamsException {
        //TODO pending to TEST
        List<User> ul = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<UserServer>> ret =
                    mServices.getAllParticipantsFromMeeting(meetingId,SOServices.PAGELIMIT,
                            offset).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            PageServer<UserServer> u = ret.body();

            for (int i = 0; i < u.getResults().size(); i++) {
                ul.add(u.getResults().get(i).toGenericModel());
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
    public boolean joinMeeting(int meetingId, int targetUserId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.joinMeeting(meetingId,targetUserId).execute();
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
    public boolean leaveMeeting(int meetingId, int targetUserId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.leaveMeeting(meetingId,targetUserId).execute();
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
    public List<Meeting> getAllMeetingsFilteredByName(String query, int page) {
        List<Meeting> l = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<MeetingServer>> res =
                    mServices.getAllMeetingsFiltered(SOServices.PAGELIMIT, offset, query).execute();
            PageServer<MeetingServer> array = res.body();
            for (int i = 0; i < array.getResults().size(); i++) {
                l.add(array.getResults().get(i).toGenericModel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public boolean addTracking(Integer userID, Integer meetingID, Float averageSpeed, Float distance, Integer steps, Long totalTimeMillis, Float calories, List<LatLng> routePoints) throws AutorizationException, ForbiddenException {
        boolean ok = true;
        TrackServer ts = new TrackServer(userID, meetingID, averageSpeed, distance, steps, totalTimeMillis, calories, routePoints);

        try {
            Response<Void> ret = mServices.addTracking(userID, meetingID, ts).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof ForbiddenException) {
                throw (ForbiddenException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public TrackingData getTracking(int userID, int meetingID) throws AutorizationException, NotFoundException {
        TrackServer m = null;
        try {
            Response<TrackServer> ret = mServices.getTracking(userID, meetingID).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            m = ret.body();
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
        return m.toGenericModel();
    }

    @Override
    public boolean deleteTrackingInMeeting(int userID, int meetingID) throws AutorizationException, NotFoundException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.deleteTracking(userID, meetingID).execute();
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
}
