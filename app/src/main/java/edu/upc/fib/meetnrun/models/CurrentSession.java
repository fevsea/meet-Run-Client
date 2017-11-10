package edu.upc.fib.meetnrun.models;

import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.persistence.WebDBController;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class CurrentSession {

    private static final CurrentSession instance = new CurrentSession();
        private String token;
        private User currentUser;
        private IGenericController controller;

        private CurrentSession() {
            controller = WebDBController.getInstance();
        }

        public static CurrentSession getInstance() {
            return instance;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public User getCurrentUser() {
            return currentUser;
        }

        public void setCurrentUser(User currentUser) {
            this.currentUser = currentUser;
        }

        public IGenericController getController(){
            return this.controller;
        }

        public void setController(IGenericController controller) {
            this.controller = controller;
        }

}
