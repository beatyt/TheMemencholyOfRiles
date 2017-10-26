package api;

/**
 * Created by user on 2016-02-20.
 */
public interface Configuration {

    enum OPTIONS {
        REPLACE_NAME_ONCE,
        USE_GENDER_MALE,
        USE_GENDER_FEMALE;

        private boolean enabled;

        public void setOption(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    String getValue(String propKey);
}

