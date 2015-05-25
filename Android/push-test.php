<?php


// API access key from Google API's Console
define( 'API_ACCESS_KEY', 'AIzaSyDAUpf_GpRVw4DQBnz7pr_aGzDEFoWJ3_8' );


$registrationIds = array("APA91bFZ_8vMJg2t6IaGAPj1z7tWPq6dpLnLsqRLrrQQlizE6OHOGC8p2t-zgkpz1C0EcFLDisMUA8yGny2ZTXZ7FthoNlKzj5ldz5ip_9zZuGQfxSNDIeMG31S96YsjX4vYjy9LR3ZQKSL033X9G9j89hCnYNAjdA" );

// prep the bundle
$msg = array
(
    'message'       => 'here is a message. message',
    'title'         => 'This is a title. title',
    'subtitle'      => 'This is a subtitle. subtitle',
    'tickerText'    => 'Ticker text here...Ticker text here...Ticker text here',
    'vibrate'   => 1,
    'sound'     => 1
);

$fields = array
(
    'registration_ids'  => $registrationIds,
    'data'              => $msg
);

$headers = array
(
    'Authorization: key=' . API_ACCESS_KEY,
    'Content-Type: application/json'
);

$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
$result = curl_exec($ch );
curl_close( $ch );

echo $result;
?>