package com.pietos.bgv.email;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    /**
     * Client Admin Welcome Email
     */
    public String clientWelcomeTemplate(String clientName,
                                        String email,
                                        String password) {

        return """
                <html>
                <body style='font-family:Arial,sans-serif'>

                    <h2>Welcome to Pietos BGV</h2>

                    <p>Dear <b>%s</b>,</p>

                    <p>Your <b>Client Admin</b> account has been created successfully.</p>

                    <table style='border-collapse:collapse'>
                        <tr>
                            <td><b>Email :</b></td>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <td><b>Temporary Password :</b></td>
                            <td>%s</td>
                        </tr>
                    </table>

                    <br>

                    <p>
                        Please login and change your password after your first login.
                    </p>

                    <br>

                    <p>
                        Regards,<br>
                        Pietos BGV Team
                    </p>

                </body>
                </html>
                """.formatted(clientName, email, password);
    }

    /**
     * Manage User Welcome Email
     */
    public String manageUserWelcomeTemplate(String name,
                                            String email,
                                            String password) {

        return """
                <html>
                <body style='font-family:Arial,sans-serif'>

                    <h2>Welcome to Pietos BGV</h2>

                    <p>Dear <b>%s</b>,</p>

                    <p>Your account has been created successfully.</p>

                    <table style='border-collapse:collapse'>
                        <tr>
                            <td><b>Email :</b></td>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <td><b>Temporary Password :</b></td>
                            <td>%s</td>
                        </tr>
                    </table>

                    <br>

                    <p>Please change your password after first login.</p>

                    <br>

                    <p>
                        Regards,<br>
                        Pietos BGV Team
                    </p>

                </body>
                </html>
                """.formatted(name, email, password);
    }
    
    /**
     * System User Welcome Email
     * (SUPER_ADMIN, ADMIN, DATA_ENTRY,
     * VERIFICATION_EXECUTIVE, QUALITY_CHECK)
     */
    public String systemUserWelcomeTemplate(String name,
                                            String email,
                                            String password,
                                            String roleName) {

        return """
                <html>
                <body style='font-family:Arial,sans-serif;background:#f5f5f5;padding:20px;'>

                    <div style='max-width:600px;background:white;padding:25px;
                                border-radius:8px;margin:auto;
                                box-shadow:0 2px 8px rgba(0,0,0,0.1);'>

                        <h2 style='color:#0d6efd;'>
                            Welcome to Pietos BGV
                        </h2>

                        <p>Dear <b>%s</b>,</p>

                        <p>
                            Your <b>%s</b> account has been created successfully.
                        </p>

                        <table style="border-collapse:collapse;margin-top:15px;">

                            <tr>
                                <td style="padding:8px;"><b>Role</b></td>
                                <td style="padding:8px;">%s</td>
                            </tr>

                            <tr>
                                <td style="padding:8px;"><b>Email</b></td>
                                <td style="padding:8px;">%s</td>
                            </tr>

                            <tr>
                                <td style="padding:8px;"><b>Temporary Password</b></td>
                                <td style="padding:8px;">%s</td>
                            </tr>

                        </table>

                        <br>

                        <p>
                            Please change your password after your first login.
                        </p>

                        <br>

                        <p>
                            Regards,<br>
                            <b>Pietos BGV Team</b>
                        </p>

                    </div>

                </body>
                </html>
                """.formatted(
                name,
                roleName,
                roleName,
                email,
                password
        );
    }
    public String manageUserWelcomeTemplate(
            String name,
            String email,
            String password,
            String roleName) {

        return """
                <html>

                <body style='font-family:Arial,sans-serif;background:#f5f5f5;padding:20px;'>

                    <div style='max-width:600px;background:white;padding:25px;
                                border-radius:8px;margin:auto;
                                box-shadow:0 2px 8px rgba(0,0,0,0.1);'>

                        <h2 style='color:#0d6efd;'>
                            Welcome to Pietos BGV
                        </h2>

                        <p>
                            Dear <b>%s</b>,
                        </p>

                        <p>
                            Your <b>Manage User</b> account has been
                            created successfully.
                        </p>

                        <table style='border-collapse:collapse;margin-top:15px;'>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Role</b>
                                </td>

                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Email</b>
                                </td>

                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Temporary Password</b>
                                </td>

                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                        </table>

                        <br>

                        <p>
                            Please change your password after your first login.
                        </p>

                        <br>

                        <p>
                            Regards,<br>
                            <b>Pietos BGV Team</b>
                        </p>

                    </div>

                </body>

                </html>
                """.formatted(
                        name,
                        roleName,
                        email,
                        password
                );
    }
    
    public String manageUserCreatedConfirmationTemplate(
            String name,
            String email,
            String roleName,
            String clientName,
            String locationName) {

        return """
                <html>

                <body style='font-family:Arial,sans-serif;background:#f5f5f5;padding:20px;'>

                    <div style='max-width:600px;background:white;padding:25px;
                                border-radius:8px;margin:auto;
                                box-shadow:0 2px 8px rgba(0,0,0,0.1);'>

                        <h2 style='color:#0d6efd;'>
                            New Manage User Created
                        </h2>

                        <p>
                            Dear Super Admin,
                        </p>

                        <p>
                            A new Manage User has been created successfully.
                        </p>

                        <table style='border-collapse:collapse;margin-top:15px;'>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Name</b>
                                </td>
                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Email</b>
                                </td>
                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Role</b>
                                </td>
                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Client</b>
                                </td>
                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:8px;'>
                                    <b>Location</b>
                                </td>
                                <td style='padding:8px;'>
                                    %s
                                </td>
                            </tr>

                        </table>

                        <br>

                        <p>
                            Regards,<br>
                            <b>Pietos BGV Team</b>
                        </p>

                    </div>

                </body>

                </html>
                """.formatted(
                        name,
                        email,
                        roleName,
                        clientName,
                        locationName
                );
    }
    
    
  
 
    
    public String internalUserCreatedConfirmationTemplate(
            String name,
            String email,
            String mobileNumber,
            String password) {

        return """
                <html>
                <body style='font-family:Arial,Helvetica,sans-serif;
                             background-color:#f5f7fa;
                             margin:0;
                             padding:20px;'>

                    <div style='max-width:650px;
                                margin:0 auto;
                                background:#ffffff;
                                padding:25px;
                                border:1px solid #e2e8f0;
                                border-radius:8px;'>

                        <h2 style='color:#123b72;'>
                            Internal User Created
                        </h2>

                        <p>Dear Super Admin,</p>

                        <p>
                            A new internal user has been created successfully.
                        </p>

                        <table style='border-collapse:collapse;
                                      margin-top:15px;
                                      width:100%;'>

                            <tr>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;
                                           background:#f1f5f9;'>
                                    <b>Name</b>
                                </td>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;'>
                                    {{NAME}}
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;
                                           background:#f1f5f9;'>
                                    <b>Email</b>
                                </td>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;'>
                                    {{EMAIL}}
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;
                                           background:#f1f5f9;'>
                                    <b>Mobile Number</b>
                                </td>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;'>
                                    {{MOBILE}}
                                </td>
                            </tr>

                            <tr>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;
                                           background:#f1f5f9;'>
                                    <b>Password</b>
                                </td>
                                <td style='padding:10px;
                                           border:1px solid #cbd5e1;'>
                                    {{PASSWORD}}
                                </td>
                            </tr>

                        </table>

                        <p style='margin-top:20px;'>
                            The internal user has been created successfully.
                        </p>

                        <p>
                            Please use the above credentials to access
                            the BGV Portal.
                        </p>

                        <br>

                        <p>
                            Regards,<br>
                            <b style='color:#123b72;'>
                                Pietos BGV Team
                            </b>
                        </p>

                    </div>

                </body>
                </html>
                """
                .replace("{{NAME}}", name)
                .replace("{{EMAIL}}", email)
                .replace("{{MOBILE}}", mobileNumber)
                .replace("{{PASSWORD}}", password);
    }
    
}