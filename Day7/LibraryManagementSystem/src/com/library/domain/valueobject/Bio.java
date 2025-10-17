package com.library.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing an author's biography.
 * This is an immutable value object that encapsulates the author's bio information.
 * Pure OOP implementation without any framework dependencies.
 */
public class Bio {
    private final String bioText;
    private final String website;
    private final String twitterHandle;
    
    protected Bio() {
        // For deserialization
        this.bioText = null;
        this.website = null;
        this.twitterHandle = null;
    }
    
    public Bio(String bioText, String website, String twitterHandle) {
        this.bioText = bioText;
        this.website = website;
        this.twitterHandle = twitterHandle != null && !twitterHandle.startsWith("@") 
            ? "@" + twitterHandle 
            : twitterHandle;
    }
    
    public String getBioText() {
        return bioText;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public String getTwitterHandle() {
        return twitterHandle;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bio bio = (Bio) o;
        return Objects.equals(bioText, bio.bioText) &&
               Objects.equals(website, bio.website) &&
               Objects.equals(twitterHandle, bio.twitterHandle);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bioText, website, twitterHandle);
    }
    
    @Override
    public String toString() {
        return "Bio{" +
                "bioText='" + (bioText != null ? bioText.substring(0, Math.min(50, bioText.length())) + "..." : "") + '\'' +
                ", website='" + website + '\'' +
                ", twitterHandle='" + twitterHandle + '\'' +
                '}';
    }
}
