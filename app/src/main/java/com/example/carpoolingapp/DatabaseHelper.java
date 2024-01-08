package com.example.carpoolingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CarpoolDB.db";

    //User Table
    private static final String TABLE_NAME = "Users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_DOB = "dateOfBirth";
    private static final String COLUMN_PHONE = "phoneNumber";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_ROLE = "role";

    //Car Table
    private static final String TABLE_CAR = "Cars";
    private static final String COLUMN_CAR_NAME = "vehicle_name";
    private static final String COLUMN_MODEL_YEAR = "model_year";
    private static final String COLUMN_IMAGE = "profile";
    private static final String COLUMN_REG_NUMBER = "registration_number";

    //Trips Table
    private static final String TABLE_TRIP = "Trips";
    private static final String COLUMN_ID = "tripID";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_DATE = "dateOfTrip";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CAR_REG = "carRegistrationNo";
    private static final String COLUMN_RATE = "ratePerKm";
    private static final String COLUMN_SEATS = "seatsAvailable";

    //Payments table
    private static final String TABLE_PAYMENTS = "Payments";
    private static final String COLUMN_PAY_ID = "paymentsID";
    private static final String COLUMN_DRIVER = "driver";
    private static final String COLUMN_PASSENGER = "passenger";
    private static final String COLUMN_PRICE = "price";



    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_GENDER + " TEXT NOT NULL,"
            + COLUMN_DOB + " TEXT NOT NULL,"
            + COLUMN_PHONE + " TEXT NOT NULL,"
            + COLUMN_ADDRESS + " TEXT NOT NULL,"
            + COLUMN_ROLE + " TEXT NOT NULL"
            + ");";

    private static final String CREATE_TABLE_CARS = "CREATE TABLE " + TABLE_CAR + " ("
            + COLUMN_REG_NUMBER + " TEXT PRIMARY KEY UNIQUE NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_IMAGE + " BLOB, "
            + COLUMN_CAR_NAME + " TEXT NOT NULL, "
            + COLUMN_MODEL_YEAR + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_EMAIL + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_EMAIL + "));";

    private static final String CREATE_TABLE_TRIPS = "CREATE TABLE " + TABLE_TRIP + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_EMAIL + " TEXT NOT NULL, "
            + COLUMN_SOURCE + " TEXT NOT NULL, "
            + COLUMN_DESTINATION + " TEXT NOT NULL, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_TIME + " TEXT NOT NULL, "
            + COLUMN_CAR_REG + " TEXT NOT NULL, "
            + COLUMN_RATE + " INTEGER NOT NULL, "
            + COLUMN_SEATS + " INTEGER NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_EMAIL + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_EMAIL + "));";

    private static final String CREATE_TABLE_PAYMENTS = "CREATE TABLE " + TABLE_PAYMENTS + " ("
            + COLUMN_PAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 2000, "
            + COLUMN_ID + " TEXT NOT NULL, "
            + COLUMN_DRIVER + " TEXT NOT NULL, "
            + COLUMN_PASSENGER + " TEXT NOT NULL, "
            + COLUMN_PRICE + " TEXT NOT NULL, "
            + "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " + TABLE_TRIP + "(" + COLUMN_ID + "));";

    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String CURRENT_USER_EMAIL_KEY = "current_user_email";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CARS);
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_PAYMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        onCreate(db);
    }


    public void addUser(String mail, String pass, String gender, String dobs, String phones, String addresss, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, mail);
        values.put(COLUMN_PASSWORD, hashPassword(pass));
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_DOB, dobs);
        values.put(COLUMN_PHONE, phones);
        values.put(COLUMN_ADDRESS, addresss);
        values.put(COLUMN_ROLE, role);
        db.insert(TABLE_NAME, null, values);
        db.close();
        Log.d("MyApp", "Added user to database. Email: " + mail + "Password: " + pass +", Gender: " + gender + ", DOB: " + dobs + ", Phone: " + phones + ", Address: " + addresss + ", Role: " + role);
    }

    public boolean addCar(Context context, @NonNull Car car, String currentUser){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, currentUser);
        Log.d("Database Helper", "addCar email:" + currentUser);
        contentValues.put(COLUMN_REG_NUMBER, car.getReg());
        contentValues.put(COLUMN_CAR_NAME, car.getCarname());
        contentValues.put(COLUMN_MODEL_YEAR, car.getModelYear());
        contentValues.put(COLUMN_IMAGE, car.getImage());
        long result = database.insert(TABLE_CAR, null, contentValues);
        database.close();
        return result != -1;
    }

    public boolean addTrip(Trip trip, String currentUser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, currentUser);
        contentValues.put(COLUMN_SOURCE, trip.getSource());
        contentValues.put(COLUMN_DESTINATION, trip.getDestination());
        contentValues.put(COLUMN_DATE, trip.getDating());
        contentValues.put(COLUMN_TIME, trip.getTiming());
        contentValues.put(COLUMN_CAR_REG, trip.getCarRegNo());
        contentValues.put(COLUMN_RATE, trip.getRatings());
        contentValues.put(COLUMN_SEATS, trip.getSeatings());
        long result = db.insert(TABLE_TRIP, null, contentValues);
        db.close();
        return result != -1;
    }

    public boolean addPayment(Paymeant paymeant, String currentUser, int tripColumnId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, tripColumnId);
        contentValues.put(COLUMN_DRIVER, paymeant.getDrive());
        contentValues.put(COLUMN_PASSENGER, currentUser);
        contentValues.put(COLUMN_PRICE, paymeant.getPricings());
        long result = db.insert(TABLE_PAYMENTS, null, contentValues);
        db.close();
        return result != -1;
    }


    public boolean checkEmail(String email) {
        String[] columns = {
                COLUMN_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    //Method to check if the email and password match in the database
    public boolean checkEmailPassword(String email, String password) {
        String[] columns = {
                COLUMN_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, hashPassword(password)};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }


    public String getRole(String email) {
        String role = "";
        String[] columns = {
                COLUMN_ROLE
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(COLUMN_ROLE);
            if (index != -1) {
                role = cursor.getString(index);
            } else {
                Log.e("ERROR", "Column " + COLUMN_ROLE + " not found in cursor");
            }
        }
        cursor.close();
        db.close();
        return role;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateUser(Userr user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GENDER, user.getGender());
        contentValues.put(COLUMN_PHONE, user.getPhone());
        contentValues.put(COLUMN_ADDRESS, user.getAddress());

        int res = db.update(TABLE_NAME, contentValues, COLUMN_EMAIL + " = ?", new String[] { user.getEmail() });
        db.close();

        return res > 0;
    }

    public void setCurrentUserEmail(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_USER_EMAIL_KEY, email);
        editor.apply();
    }



    public String getCurrentUserEmail(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String currentUserId = sharedPreferences.getString("currentUserId", "");
        String query = "SELECT " + COLUMN_EMAIL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{currentUserId});
        String email = "";

        if (cursor.moveToFirst()) {
            int emailColumnIndex = cursor.getColumnIndex(COLUMN_EMAIL);

            if(emailColumnIndex != -1){
                email = cursor.getString(emailColumnIndex);
            }
        }
        cursor.close();
        db.close();
        return email;
    }

    public Userr getUser(String userEmail){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_GENDER, COLUMN_DOB, COLUMN_PHONE, COLUMN_ADDRESS};
        String selection = COLUMN_EMAIL + "=?";
        String[] selectionArgs = {userEmail};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        Userr userr = null;

        if(cursor.moveToFirst()){
            do{
                int mailColumnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int genColumnIndex = cursor.getColumnIndex(COLUMN_GENDER);
                int dateColumnIndex = cursor.getColumnIndex(COLUMN_DOB);
                int phoneColumnIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int addColumnIndex = cursor.getColumnIndex(COLUMN_ADDRESS);

                String email = cursor.getString(mailColumnIndex);
                String gend = cursor.getString(genColumnIndex);
                String dat = cursor.getString(dateColumnIndex);
                String phone = cursor.getString(phoneColumnIndex);
                String address = cursor.getString(addColumnIndex);

                userr = new Userr(email, null, gend, dat, phone, address);


            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userr;
    }

    public List<Userr> getAllCarOwners() {
        List<Userr> carOwnersList = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_EMAIL + ", " + COLUMN_PHONE + ", " + COLUMN_ADDRESS +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ROLE + " = 'Car_Owner'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                int emailColumnIndeex = cursor.getColumnIndex(COLUMN_EMAIL);
                int phonnColumnIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int addColumnIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                String email = cursor.getString(emailColumnIndeex);
                String phone = cursor.getString(phonnColumnIndex);
                String address = cursor.getString(addColumnIndex);

                Userr user = new Userr(email, null, null, null, phone, address);
                carOwnersList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return carOwnersList;
    }

    public List<Userr> getAllPassengers() {
        List<Userr> passenger = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_EMAIL + ", " + COLUMN_PHONE + ", " + COLUMN_ADDRESS +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ROLE + " = 'Passenger'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                int emailColumnIndeex = cursor.getColumnIndex(COLUMN_EMAIL);
                int phonnColumnIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int addColumnIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                String email = cursor.getString(emailColumnIndeex);
                String phone = cursor.getString(phonnColumnIndex);
                String address = cursor.getString(addColumnIndex);

                Userr user = new Userr(email, null, null, null, phone, address);
                passenger.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return passenger;
    }


    public List<Car> getCarsList(String currentEmail){
        List<Car> carList = new ArrayList<>();
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        String querry = "SELECT * FROM " + TABLE_CAR + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor curssor = liteDatabase.rawQuery(querry, new String[]{currentEmail});

        if(curssor.moveToFirst()){
            do{
                int regColumnIndex = curssor.getColumnIndex(COLUMN_REG_NUMBER);
                int nameColumnIndex = curssor.getColumnIndex(COLUMN_CAR_NAME);
                int yearColumnIndex = curssor.getColumnIndex(COLUMN_MODEL_YEAR);
                int imageColumnIndex = curssor.getColumnIndex(COLUMN_IMAGE);

                String regisNumber = curssor.getString(regColumnIndex);
                String vehName = curssor.getString(nameColumnIndex);
                int yearOfModel = curssor.getInt(yearColumnIndex);
                byte[] vehicleImage = curssor.getBlob(imageColumnIndex);

                Car vehicle = new Car(currentEmail, regisNumber, vehName, yearOfModel,vehicleImage);
                carList.add(vehicle);
            }while (curssor.moveToNext());
        }
        curssor.close();
        liteDatabase.close();
        return carList;
    }

    public List<String> getRegistrations(String email){
        List<String> regges = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_REG_NUMBER + " FROM " + TABLE_CAR + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email});

        if(cursor.moveToFirst()){
            do{
                regges.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return regges;
    }

    public List<Trip> getTripList(String email){
        List<Trip> tripList = new ArrayList<>();
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        String querry = "SELECT * FROM " + TABLE_TRIP + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor curssor = liteDatabase.rawQuery(querry, new String[]{email});

        if(curssor.moveToFirst()){
            do{
                int sourceColumnIndex = curssor.getColumnIndex(COLUMN_SOURCE);
                int destinColumnIndex = curssor.getColumnIndex(COLUMN_DESTINATION);
                int dayColumnIndex = curssor.getColumnIndex(COLUMN_DATE);
                int timColumnIndex = curssor.getColumnIndex(COLUMN_TIME);
                int carColumnIndex = curssor.getColumnIndex(COLUMN_CAR_REG);
                int rateColumnIndex = curssor.getColumnIndex(COLUMN_RATE);
                int seatColumnIndex = curssor.getColumnIndex(COLUMN_SEATS);

                String origins = curssor.getString(sourceColumnIndex);
                String destiny = curssor.getString(destinColumnIndex);
                String dayy = curssor.getString(dayColumnIndex);
                String timme = curssor.getString(timColumnIndex);
                String carReg = curssor.getString(carColumnIndex);
                int rates = curssor.getInt(rateColumnIndex);
                int seats = curssor.getInt(seatColumnIndex);

                Trip tripp = new Trip(origins, destiny, dayy, timme, carReg, rates, seats);
                tripList.add(tripp);
            }while (curssor.moveToNext());
        }
        curssor.close();
        liteDatabase.close();
        return tripList;
    }

    public List<Trip> getTripsList(){
        List<Trip> tripList = new ArrayList<>();
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        String querry = "SELECT " + TABLE_TRIP + ".*, " + TABLE_NAME + ".phoneNumber "
                + "FROM " + TABLE_TRIP + " INNER JOIN " + TABLE_NAME + " ON " + TABLE_TRIP + ".email = " + TABLE_NAME + ".email";
        Cursor curssor = liteDatabase.rawQuery(querry, null);

        if(curssor.moveToFirst()){
            do{
                int emailColumnIndex = curssor.getColumnIndex(COLUMN_EMAIL);
                int sourceColumnIndex = curssor.getColumnIndex(COLUMN_SOURCE);
                int destinColumnIndex = curssor.getColumnIndex(COLUMN_DESTINATION);
                int dayColumnIndex = curssor.getColumnIndex(COLUMN_DATE);
                int timColumnIndex = curssor.getColumnIndex(COLUMN_TIME);
                int carColumnIndex = curssor.getColumnIndex(COLUMN_CAR_REG);
                int rateColumnIndex = curssor.getColumnIndex(COLUMN_RATE);
                int seatColumnIndex = curssor.getColumnIndex(COLUMN_SEATS);
                int phonColumnIndex = curssor.getColumnIndex("phoneNumber");

                String emailing = curssor.getString(emailColumnIndex);
                String origins = curssor.getString(sourceColumnIndex);
                String destiny = curssor.getString(destinColumnIndex);
                String dayy = curssor.getString(dayColumnIndex);
                String timme = curssor.getString(timColumnIndex);
                String carReg = curssor.getString(carColumnIndex);
                int rates = curssor.getInt(rateColumnIndex);
                int seats = curssor.getInt(seatColumnIndex);
                String phun = curssor.getString(phonColumnIndex);

                Trip tripp = new Trip(origins, destiny, dayy, timme, carReg, rates, seats);
                tripp.setMerlin(emailing);
                tripp.setPhoon(phun);
                tripList.add(tripp);
            }while (curssor.moveToNext());
        }
        curssor.close();
        liteDatabase.close();
        return tripList;
    }

    public int getTripId(String driver, String date, String time){
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        String querry = "SELECT * FROM " + TABLE_TRIP + " WHERE " + COLUMN_EMAIL + " = ? AND "
                + COLUMN_DATE + " = ? AND " + COLUMN_TIME + " = ? ";
        Cursor cursor = liteDatabase.rawQuery(querry, new String[]{driver, date, time});

        int tripId = -1;

        if (cursor.moveToFirst()){
            int tripColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            if (tripColumnIndex >= 0) {
                tripId = cursor.getInt(tripColumnIndex);
            }
        }

        cursor.close();
        liteDatabase.close();

        return tripId;
    }

    public byte[] getCarImage(String email, String carRegistration){
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "SELECT " + COLUMN_IMAGE + " FROM " + TABLE_CAR + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_REG_NUMBER + " = ?" ;
        Cursor cursor = db.rawQuery(querry, new String[]{email, carRegistration});

        byte[] image = null;

        if(cursor.moveToFirst()){
            int imageColumnIndex = cursor.getColumnIndex(COLUMN_IMAGE);
            image = cursor.getBlob(imageColumnIndex);
        }
        cursor.close();
        db.close();
        return image;
    }

    public ArrayList<Paymeant> getPayments() {
        ArrayList<Paymeant> paymentsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PAYMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int driverColumnIndex = cursor.getColumnIndex(COLUMN_DRIVER);
                int priceColumnIndex = cursor.getColumnIndex(COLUMN_PRICE);
                int passColumnIndex = cursor.getColumnIndex(COLUMN_PASSENGER);

                String driver = cursor.getString(driverColumnIndex);
                int prices = cursor.getInt(priceColumnIndex);
                String passenger = cursor.getString(passColumnIndex);

                Paymeant payment = new Paymeant(driver, prices);
                payment.setDrive(driver);
                payment.setPricings(prices);
                payment.setPassenger(passenger);

                paymentsList.add(payment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return paymentsList;
    }

    public List<Trip> getBookedTrips(String email){
        List<Trip> ctrip = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_DESTINATION + ", " + COLUMN_TIME + ", " + COLUMN_EMAIL + ", " +
                TABLE_PAYMENTS + "." + COLUMN_PRICE + " FROM " + TABLE_TRIP +
                " INNER JOIN " + TABLE_PAYMENTS + " ON " + TABLE_TRIP + "." + COLUMN_ID + " = " + TABLE_PAYMENTS + ".tripID " +
                "WHERE " + TABLE_PAYMENTS + "." + COLUMN_PASSENGER + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});
        if (cursor.moveToFirst()) {
            do {

                int desteColumnIndex = cursor.getColumnIndex(COLUMN_DESTINATION);
                int trimColumnIndex = cursor.getColumnIndex(COLUMN_TIME);
                int driveeColumnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int payColumnIndex = cursor.getColumnIndex(COLUMN_PRICE);

                String destination = cursor.getString(desteColumnIndex);
                String time = cursor.getString(trimColumnIndex);
                String driverEmail = cursor.getString(driveeColumnIndex);
                String paymentt = cursor.getString(payColumnIndex);

                Trip trip = new Trip(null, destination, null,  time, null, 0, 0);
                trip.setMerlin(driverEmail);
                trip.setPayment(paymentt);
                ctrip.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ctrip;
    }

    public List<Trip> getListTrips(String email){
        List<Trip> ctrip = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_DESTINATION + ", " + COLUMN_EMAIL + ", " +
                TABLE_PAYMENTS + "." + COLUMN_PRICE + " FROM " + TABLE_TRIP +
                " INNER JOIN " + TABLE_PAYMENTS + " ON " + TABLE_TRIP + "." + COLUMN_ID + " = " + TABLE_PAYMENTS + ".tripID " +
                "WHERE " + TABLE_PAYMENTS + "." + COLUMN_PASSENGER + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});
        if (cursor.moveToFirst()) {
            do {

                int desteColumnIndex = cursor.getColumnIndex(COLUMN_DESTINATION);
                int trimColumnIndex = cursor.getColumnIndex(COLUMN_TIME);
                int driveeColumnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int payColumnIndex = cursor.getColumnIndex(COLUMN_PRICE);

                String destination = cursor.getString(desteColumnIndex);
                String time = cursor.getString(trimColumnIndex);
                String driverEmail = cursor.getString(driveeColumnIndex);
                String paymentt = cursor.getString(payColumnIndex);

                Trip trip = new Trip(null, destination, null,  time, null, 0, 0);
                trip.setMerlin(driverEmail);
                trip.setPayment(paymentt);
                ctrip.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ctrip;
    }

    public ArrayList<Trip> getPassengerTrips(String passengerEmail) {
        ArrayList<Trip> tripsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PAYMENTS + " JOIN " + TABLE_TRIP
                + " ON " + TABLE_PAYMENTS + ".tripID" + " = " + TABLE_TRIP + "." + COLUMN_ID
                + " WHERE " + COLUMN_PASSENGER + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{passengerEmail});

        if (cursor.moveToFirst()) {
            do {
                int orColumnIndex = cursor.getColumnIndex(COLUMN_SOURCE);
                int dColumnIndex = cursor.getColumnIndex(COLUMN_DESTINATION);
                int tColumnIndex = cursor.getColumnIndex(COLUMN_TIME);

                String ori = cursor.getString(orColumnIndex);
                String dea = cursor.getString(dColumnIndex);
                String tie = cursor.getString(tColumnIndex);

                Trip t = new Trip(ori, dea, null, tie, null, 0, 0);
                tripsList.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tripsList;
    }


    public boolean deleteCar(Context context, String registration, String email){
        SQLiteDatabase datab = this.getWritableDatabase();
        String whereClause = COLUMN_REG_NUMBER + " = ? AND " + COLUMN_EMAIL + " = ?";
        String[] whereArgs = {registration, email};
        int deletedCars = datab.delete(TABLE_CAR, whereClause, whereArgs);
        datab.close();
        return deletedCars > 0;
    }

    public boolean deleteUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();

        int deleteRows = db.delete(TABLE_NAME, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();

        return deleteRows > 0;
    }

}
