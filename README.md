# KDC_symmetric_chat
A Key Agremeent System, based on Key Distribution Center.

- Alice and Bob prevously shared a symmetric key (KA and KB) with the KDC;
- Alice send a master key request to KDC;
- KDC verifies the request, and answer with the a message encrypted with KA. In the message we also found a token, with the key, encrypted with KB;
- Alice sends the token to Bob;
- Bob extract the master key and make a challenge/response protocol on the master key knowledge.

Then Alice and Bob use the master + session key to share some messages.
