package com.dut.banana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.banana.R;
import com.dut.banana.Song;
import com.dut.banana.lib.Config;
import com.dut.banana.lib.SharedObject;

public class RawLyricActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_lyric);
        inputView = (EditText) findViewById(R.id.raw_lyric_et_input);
        Button btnGo = (Button) findViewById(R.id.raw_lyric_btn_go);
        btnGo.setOnClickListener(this);

        Song song = (Song) SharedObject.getInstance().get(Config.SHARED_CURRENT_SONG);
        TextView tvSong = (TextView) findViewById(R.id.lyric_raw_tv_song);
        tvSong.setText(String.format("%s - %s", song.getTitle(), song.getArtist()));

        String content = "Anh trông theo đó, đây\n" +
                "Nhưng sao chẳng thấy\n" +
                "Những dấu son còn tươi trên môi hồng em từ ngày em đi\n" +
                "Khi bao nhiêu khó, khăn\n" +
                "Bên anh nhiều lắm\n" +
                "Em nói em sẽ về đây khi mùa đông tàn\n" +
                "Anh nhớ em khi cơn mưa dài lạnh lùng chưa tan\n" +
                "Anh nhớ em khi em ôm chầm vào lòng anh khóc oà\n" +
                "Anh nhớ em khi trên vai mình còn nhiều âu lo\n" +
                "Anh nhớ em, khi trăng vừa lên, ban đêm dài hơn ban ngày\n" +
                "\n" +
                "Cuz I Know...\n" +
                "Tim em bao la, mặc kệ đợi chờ người ta\n" +
                "Cho đi bao nhiêu, nhận về khói sương thật nhiều\n" +
                "Nơi đây bao xa, để tìm lại mình ngày qua\n" +
                "Oh no uh oh…oh uh oh lời em hứa sẽ trở về...\n" +
                "Give me ur Heart…Give me ur Love\n" +
                "Mùa đông bên anh sao thật dài, còn em đang đi đâu miệt mài\n" +
                "Give me ur Heart…Give me ur Love\n" +
                "Mùa đông thương anh ôm mệt nhoài, vì em đi đâu anh chờ hoài\n" +
                "\n" +
                "Căn phòng vẫn vậy \n" +
                "Chẳng gì ngoài những hối tiếc , nỗi nhớ em và bass\n" +
                "Ở 1 nơi nào đó anh không hay , em liệu bây giờ đây ??\n" +
                "Vẫn còn nghe những bài hát này hay đang say trong vòng tay... \n" +
                "Thật cô đơn khi anh luôn nghĩ về em sớm tối\n" +
                "Nhớ mắt môi \n" +
                "Lạc vào em , anh nhớ cả những chiều tan về đưa đón lối \n" +
                "Và nhớ mùa tuyết rơi \n" +
                "Anh chẳng muốn tìm về quá khứ nữa , cứ để nó đẹp và ngủ yên thôi \n" +
                "Chỉ là những tấm hình , khói thuốc , chai rượu cạn dần và 2 giờ đêm trôi…\n" +
                "\n" +
                "Cuz I Know...\n" +
                "Tim em bao la, mặc kệ đợi chờ người ta\n" +
                "Cho đi bao nhiêu, nhận về khói sương thật nhiều\n" +
                "Nơi đây bao xa, để tìm lại mình ngày qua\n" +
                "Oh no uh oh…oh uh oh lời em hứa sẽ trở về...\n" +
                "Give me ur Heart…Give me ur Love\n" +
                "Mùa đông bên anh sao thật dài, còn em đang đi đâu miệt mài\n" +
                "Give me ur Heart…Give me ur Love\n" +
                "Mùa đông thương anh ôm mệt nhoài, vì em đi đâu anh chờ hoài\n" +
                "\n" +
                "Ooh Ooh\n" +
                "Sao không như lời em nói khi hơi ấm đang đầy vơi\n" +
                "Ngày dài qua…\n" +
                "Anh chưa bao giờ đi hết con đường trắng mưa buồn rơi yea\n" +
                "And Ooh Ooh\n" +
                "Cho đi những điều mãi mãi anh đổi lấy những tàn phai này\n" +
                "Còn gì đâu nào? Buông xuôi hy vọng tàn úa bên mùa đông vô tình trôi\n" +
                "\n" +
                "Cuz I know…i know know, i know know\n" +
                "Tim em bao la, mặc kệ đợi chờ người ta\n" +
                "Cho đi bao nhiêu\n" +
                "Bao nhiêu chỉ là sương khói thôi\n" +
                "Cuz I know, i know ngày qua uh oh. oh uh oh\n" +
                "Lời em hứa sẽ trở về đây\n" +
                "Give me ur Heart…Give me ur Love\n" +
                "Mùa đông sao bên anh thật dài, còn em đang đi đâu miệt mài\n" +
                "Give me ur Heart…Give me ur Love\n" +
                "Mùa đông thương anh ôm mệt nhoài, no no no no em đi mãi không trở về…\n" +
                "\n" +
                "Hoa Sữa rơi vây đầy quanh trên đường nơi anh thường đi với em\n" +
                "Hoa Sữa mang anh gần em khi ngày qua đợi chờ gió đem hương về\n" +
                "Hoa Sữa rơi anh nhặt lên đưa vào tay em đặt trên mái tóc em…\n" +
                "Hoa Sữa ơi sao để bên em dài lâu đây anh đếm cánh hoa này...";
        inputView.setText(content);
    }

    @Override
    public void onClick(View v) {
        String content = inputView.getText().toString();
        if (content.length() > 0) {
            Intent intent = new Intent(this, LyricEditorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Config.INPUT_LYRIC_EDITOR, content);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(RawLyricActivity.this, "Nothing here! you should enter some song's lyric to edit in next step!", Toast.LENGTH_SHORT).show();
        }
    }
}
