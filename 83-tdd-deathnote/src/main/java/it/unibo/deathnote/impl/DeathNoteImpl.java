package it.unibo.deathnote.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import it.unibo.deathnote.api.DeathNote;


public class DeathNoteImpl implements DeathNote {
    private static final int INDEX_OF_CAUSE = 0;
    private static final int INDEX_OF_DETAILS = 1;
    private static final int TIME_EXPIRATION_CAUSE = 40; // Represent 40 milliseconds.
    private static final int TIME_EXPIRATION_DETAILS = 6040; // Represent 6 seconds in milliseconds plus 40 milliseconds.


    private Map<String, List<String>> namesWritten;
    private String lastNameWritten; 
    private long writtenNameTime;
    private long writtenDeathCauseTime;


    /*
     * Constructor(s).
     */

    public DeathNoteImpl(){
        this.namesWritten = new LinkedHashMap<String, List<String>>();
    }

    /*
     * DeathNote methods.
     */

    @Override
    public String getRule(int ruleNumber) {
        checkGetRule(ruleNumber);
        return DeathNote.RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(String name) {
        checkWriteName(name);
        namesWritten.put(name, new ArrayList<>());
        lastNameWritten = name;

        writtenNameTime = System.currentTimeMillis();
    }

    @Override
    public boolean writeDeathCause(String cause) {
        checkCauseDetails(cause);
        long timeSpan = System.currentTimeMillis() - writtenNameTime;
        
        boolean timeSpanCondition = timeSpan <= TIME_EXPIRATION_CAUSE;

        if( timeSpanCondition ){
            namesWritten.get(lastNameWritten).add(INDEX_OF_CAUSE, cause);
        }

        writtenDeathCauseTime = System.currentTimeMillis();

        return ( timeSpanCondition ) ? true : false;
    }

    @Override
    public boolean writeDetails(String details)  {
        checkCauseDetails(details);    
        long timeSpan = (System.currentTimeMillis() - writtenDeathCauseTime);
        
        boolean timeSpanCondition = timeSpan <= TIME_EXPIRATION_DETAILS;

        if( timeSpanCondition ){
            namesWritten.get(lastNameWritten).add(INDEX_OF_DETAILS, details);
        }


        return ( timeSpanCondition ) ? true : false;
    }

    @Override
    public String getDeathCause(String name) {
        checkGetDeathCauseDetails(name);
        return ( namesWritten.get(name).size() < 1 ) ? "" : namesWritten.get(name).get(INDEX_OF_CAUSE);
    }

    @Override
    public String getDeathDetails(String name) {
        checkGetDeathCauseDetails(name);
        return ( namesWritten.get(name).size() < 2 ) ? "" : namesWritten.get(name).get(INDEX_OF_DETAILS);
    }

    @Override
    public boolean isNameWritten(String name) {
        return namesWritten.containsKey(name);
    }

    /*
     * Check methods
     */

    private void checkGetRule(int ruleNumber) {
        if ( ruleNumber < 1 || ruleNumber > DeathNote.RULES.size()){
            throw new IllegalArgumentException("The role number must be between 1 and the rules size.");
        }
    }

    private void checkWriteName(String name) {
        if ( name == null ){
            throw new NullPointerException("The name to write cannot be null!");
        }
    }

    private void checkCauseDetails(String causeOrDetails) {
        if ( namesWritten.size() == 0 || causeOrDetails == null ){
            throw new IllegalStateException("There is no name written in this DeathNote, or the cause/detail was null.");
        }
    }

    private void checkGetDeathCauseDetails(String name) {
        if( !namesWritten.containsKey(name) ){
            throw new IllegalArgumentException("The name passed was not written in the DeathNote.");
        }
    }
    
}
