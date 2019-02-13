# My-AndroidProjects-aidlDemo
#调用方法

@
McUtils.message(ri)
                        .url(url)
                        .tag(this)
                        .param("flag", "getData")
                        .param("apply", "申请上路")
                        .execute(new StringCallback() {
                            @Override
                            public void onFinish(final String response) {
                                tv.setText("获取数据结果：" + response);
                            }

                            @Override
                            public void onError(String response) {
                                tv.setText("获取数据结果：" + response);
                            }
                        });
