package com.cesarvillarreal.portfolio.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form-backing object for {@code POST /contact}.
 * <p>
 * The {@link ContactMessage} entity used to be bound directly as the form object, which made
 * every one of its persistent fields settable from the request. Posting {@code id=1} bound
 * straight through to {@code setId()}, and saving an entity with an id is an UPDATE — so a
 * submission could overwrite an existing message. This record exists so that {@code id},
 * {@code read} and {@code createdAt} have no binding path at all.
 * <p>
 * Anything added here becomes settable by an anonymous visitor. Nothing that is not part of
 * the visible form belongs in this record.
 *
 * @param website honeypot. Hidden off-screen in the template, so a human never fills it in
 *                and a bot that fills every field does.
 */
public record ContactForm(

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 255, message = "Email must be less than 255 characters")
        String email,

        @NotBlank(message = "Message is required")
        @Size(min = 10, max = 5000, message = "Message must be between 10 and 5000 characters")
        String message,

        String website
) {

    /** An empty form, for the initial GET render. */
    public static ContactForm empty() {
        return new ContactForm("", "", "", "");
    }

    /**
     * True when the honeypot was filled in. Callers must fail <em>silently</em> — telling a bot
     * it was detected just teaches it which field to skip.
     */
    public boolean isLikelyBot() {
        // isEmpty(), deliberately not isBlank(): a browser submits "" for a field nobody
        // touched, so any non-empty value at all — including a single space — means something
        // filled it in. isBlank() would wave through a bot that pads every field with
        // whitespace, which is exactly the kind of bot this is meant to catch.
        return website != null && !website.isEmpty();
    }
}
