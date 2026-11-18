package com.estudioderecho.faseprivada;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(11,18,32);
    private final int panel = Color.rgb(21,31,49);
    private final int gold = Color.rgb(214,173,98);
    private final int white = Color.rgb(245,247,251);
    private final int muted = Color.rgb(155,169,189);
    private Database db;
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Integer> order = new ArrayList<>();
    private int position = 0, cycle = 1;
    private TextView counter, cycleView, questionView, answerView, status;
    private LinearLayout root;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        db = new Database();
        questions = db.getQuestions();
        buildScreen();
        if (questions.isEmpty()) {
            status.setText("No se encontraron preguntas en la base de datos incluida.");
        } else {
            startCycle();
            render();
        }
    }

    private void buildScreen() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(18));
        root.setBackgroundColor(bg);

        TextView eyebrow = text("PREPARACIÓN · FASE PRIVADA", 12, gold, true);
        eyebrow.setGravity(Gravity.CENTER);
        root.addView(eyebrow);

        TextView title = text("Estudio de Derecho", 28, white, true);
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(-1, -2);
        titleLp.setMargins(0, dp(7), 0, dp(3));
        root.addView(title, titleLp);

        TextView subtitle = text("Preguntas y respuestas · Modo sin conexión", 14, muted, false);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        LinearLayout meta = new LinearLayout(this);
        meta.setOrientation(LinearLayout.HORIZONTAL);
        meta.setGravity(Gravity.CENTER_VERTICAL);
        meta.setPadding(0, dp(25), 0, dp(14));
        counter = text("0 preguntas", 13, muted, false);
        cycleView = text("Ciclo 1", 13, muted, false);
        meta.addView(counter, new LinearLayout.LayoutParams(0, -2, 1));
        meta.addView(cycleView);
        root.addView(meta);

        ScrollView scroll = new ScrollView(this);
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(22), dp(20), dp(24));
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(panel);
        shape.setCornerRadius(dp(20));
        shape.setStroke(dp(1), Color.rgb(43,56,80));
        card.setBackground(shape);

        card.addView(text("PREGUNTA", 12, gold, true));
        questionView = text("", 21, white, true);
        questionView.setLineSpacing(dp(5), 1f);
        LinearLayout.LayoutParams qlp = new LinearLayout.LayoutParams(-1, -2);
        qlp.setMargins(0, dp(12), 0, dp(24));
        card.addView(questionView, qlp);

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(43,56,80));
        card.addView(divider, new LinearLayout.LayoutParams(-1, dp(1)));

        TextView answerLabel = text("RESPUESTA", 12, gold, true);
        LinearLayout.LayoutParams alp = new LinearLayout.LayoutParams(-1, -2);
        alp.setMargins(0, dp(20), 0, dp(10));
        card.addView(answerLabel, alp);
        answerView = text("", 16, white, false);
        answerView.setLineSpacing(dp(5), 1f);
        card.addView(answerView);
        scroll.addView(card);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        Button next = button("Siguiente pregunta  →", gold, Color.rgb(25,25,25));
        LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(-1, dp(54));
        nlp.setMargins(0, dp(18), 0, dp(9));
        root.addView(next, nlp);
        next.setOnClickListener(v -> nextQuestion());

        Button reset = button("Reiniciar recorrido", Color.rgb(37,51,74), white);
        root.addView(reset, new LinearLayout.LayoutParams(-1, dp(48)));
        reset.setOnClickListener(v -> { cycle = 1; startCycle(); render(); });

        status = text("Banco integrado en la aplicación.", 12, muted, false);
        status.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(-1, -2);
        slp.setMargins(0, dp(12), 0, 0);
        root.addView(status, slp);

        setContentView(root);
    }

    private void nextQuestion() {
        if (questions.isEmpty()) return;
        position++;
        if (position >= order.size()) { cycle++; startCycle(); }
        render();
    }

    private void startCycle() {
        order.clear();
        for (int i = 0; i < questions.size(); i++) order.add(i);
        Collections.shuffle(order);
        position = 0;
    }

    private void render() {
        if (questions.isEmpty()) return;
        Question q = questions.get(order.get(position));
        questionView.setText((q.number > 0 ? "N.º " + q.number + "\n\n" : "") + q.question);
        answerView.setText(q.answer);
        counter.setText("Pregunta " + (position + 1) + " de " + questions.size());
        cycleView.setText("Ciclo " + cycle);
        status.setText("Sin conexión · " + questions.size() + " preguntas incluidas");
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private Button button(String value, int color, int textColor) {
        Button b = new Button(this);
        b.setText(value);
        b.setTextSize(15);
        b.setAllCaps(false);
        b.setTextColor(textColor);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(13));
        b.setBackground(d);
        return b;
    }

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    private class Database extends SQLiteOpenHelper {
        Database() { super(MainActivity.this, "estudio_derecho.db", null, 1); }
        @Override public void onCreate(SQLiteDatabase database) {
            database.execSQL("CREATE TABLE questions (_id INTEGER PRIMARY KEY AUTOINCREMENT, number INTEGER, page INTEGER, question TEXT NOT NULL, answer TEXT NOT NULL)");
            seed(database);
        }
        @Override public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) { }
        private void seed(SQLiteDatabase database) {
            try (InputStream in = getAssets().open("preguntas.json");
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192]; int n;
                while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                JSONArray array = new JSONArray(out.toString(StandardCharsets.UTF_8.name()));
                database.beginTransaction();
                try {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.put("number", item.optInt("numero"));
                        values.put("page", item.optInt("pagina"));
                        values.put("question", item.getString("pregunta"));
                        values.put("answer", item.getString("respuesta"));
                        database.insertOrThrow("questions", null, values);
                    }
                    database.setTransactionSuccessful();
                } finally { database.endTransaction(); }
            } catch (Exception e) { android.util.Log.e("StudyDB", "No se pudo cargar el banco", e); }
        }
        ArrayList<Question> getQuestions() {
            ArrayList<Question> list = new ArrayList<>();
            SQLiteDatabase database = getReadableDatabase();
            try (Cursor c = database.rawQuery("SELECT number, page, question, answer FROM questions ORDER BY _id", null)) {
                while (c.moveToNext()) list.add(new Question(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3)));
            }
            return list;
        }
    }

    private static class Question {
        final int number, page; final String question, answer;
        Question(int n, int p, String q, String a) { number=n; page=p; question=q; answer=a; }
    }
}
