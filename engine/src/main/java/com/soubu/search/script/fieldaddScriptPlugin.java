package com.soubu.search.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.soubu.search.base.ScoreAnalyzer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soubu.search.analyzer.ScoreAnalyzerFactoryImpl;
import com.soubu.search.base.ScoreAnalyzerFactory;
import com.soubu.search.score.Entry;
import com.soubu.search.score.ScoreConst;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.apache.lucene.index.LeafReaderContext;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.*;
import org.elasticsearch.search.lookup.LeafSearchLookup;
import org.elasticsearch.search.lookup.SearchLookup;


/**
 * An example script plugin that adds a {@link ScriptEngineService} implementing expert scoring.
 */
public class fieldaddScriptPlugin extends Plugin implements ScriptPlugin {

    @Override
    public ScriptEngineService getScriptEngineService(Settings settings) {
        return new MyExpertScriptEngine();
    }

    /**
     * An example {@link ScriptEngineService} that uses Lucene segment details to implement pure document frequency scoring.
     */
    // tag::expert_engine
    private static class MyExpertScriptEngine implements ScriptEngineService {


        @Override
        public String getType() {
            return "expert_scripts";
        }

        @Override
        public Object compile(String scriptName, String scriptSource, Map<String, String> params) {
            if (!scriptSource.equals("")) {
                return scriptSource;
            }else {
                return "magic_score_script";
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public SearchScript search(CompiledScript compiledScript, final SearchLookup lookup, @Nullable Map<String, Object> vars) {

            /**
             * 校验输入参数，DSL中params 参数列表
             */
            final String magic;
            double base = 100d;
            List<ScoreAnalyzer> scoreAnalyzers = new ArrayList<>();
            ScoreAnalyzerFactory analyzerFactory = ScoreAnalyzerFactoryImpl.getInstance();

            if (vars == null || vars.isEmpty() || !vars.containsKey(ScoreConst.MAGIC_KEY)) {
                throw new IllegalStateException("MagicScoreScript params can`t be null");
            }
            Object baseObj = vars.get(ScoreConst.BASE_KEY);
            if (baseObj != null) {
                if (Number.class.isInstance(baseObj)) {
                    base = ((Number) baseObj).doubleValue();
                } else {
                    base = Double.valueOf(baseObj.toString());
                }
            }
            // magic params
             magic = (String) vars.get(ScoreConst.MAGIC_KEY);
            JSONArray props = JSON.parseArray(magic);
            if (scoreAnalyzers == null) {
                scoreAnalyzers = new ArrayList<>(props.size() + 1);
            } else {
                scoreAnalyzers.clear();
            }
            for (int i = 0; i < props.size(); i++) {
                JSONObject prop = props.getJSONObject(i);
                ScoreProperties scoreProperties = analyzeScoreProps(prop);
                ScoreAnalyzer analyzer = analyzerFactory.create(scoreProperties);
                scoreAnalyzers.add(analyzer);
            }

            List<ScoreAnalyzer> finalScoreAnalyzers = scoreAnalyzers;
            double finalBase = base;
            return new SearchScript() {
                @Override
                public LeafSearchScript getLeafSearchScript(LeafReaderContext context) throws IOException {
                    final LeafSearchLookup leafLookup = lookup.getLeafSearchLookup(context);

                    return new LeafSearchScript() {
                        @Override
                        public void setDocument(int doc) {
                            if (leafLookup != null) {
                                leafLookup.setDocument(doc);
                            }
                        }

                        @Override
                        public double runAsDouble() {
                            double finalScore = 0;

                            for (ScoreAnalyzer analyzer : finalScoreAnalyzers) {
                                Object valueObj = leafLookup.doc().get(analyzer.field());
                                if (valueObj != null) {
                                    if (ScriptDocValues.Longs.class.isInstance(valueObj)) {
                                        finalScore += analyzer.analyze(((ScriptDocValues.Longs) valueObj).getValue());
                                    } else if (ScriptDocValues.Doubles.class.isInstance(valueObj)) {
                                        finalScore += analyzer.analyze(((ScriptDocValues.Doubles) valueObj).getValue());
                                    } else {
                                        throw new IllegalStateException("Field[" + analyzer.field() + "] is not number field");
                                    }
                                }
                            }
                            if (finalScore > 280) {
                                int iii = 0;
                            }
                            return (finalScore / finalBase) + 1;
                        }
                    };
                }

                @Override
                public boolean needsScores() {
                    return false;
                }
            };
        }

        @Override
        public ExecutableScript executable(CompiledScript compiledScript, @Nullable Map<String, Object> params) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isInlineScriptEnabled() {
            return true;
        }

        @Override
        public void close() {
        }

        private static ScoreProperties analyzeScoreProps(JSONObject prop) {
            ScoreProperties scoreProperties = new ScoreProperties();
            scoreProperties.setType(ScoreType.valueOf(prop.getString("type")));
            scoreProperties.setField(prop.getString("field"));
            JSONArray entrys = prop.getJSONArray("param");
            Entry[] param = new Entry[entrys.size()];
            scoreProperties.setParam(param);
            for (int ii = 0; ii < entrys.size(); ii++) {
                JSONObject entry = entrys.getJSONObject(ii);
                Entry et = new Entry();
                et.setKey(entry.getString("key"));
                et.setValue(entry.getString("value"));
                param[ii] = et;
            }
            return scoreProperties;
        }
    }
    // end::expert_engine
}