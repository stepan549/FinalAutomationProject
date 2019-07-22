Feature: Test resending email to another user

    Scenario: Test resending mail
        Given I am in mailbox page
        When I open first mail
        When I click resend button
        When I enter "aleksandr.sats@gmail.com"
        When I click to send
        Then I see message about send mail