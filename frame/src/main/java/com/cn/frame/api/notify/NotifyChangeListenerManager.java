package com.cn.frame.api.notify;

import android.support.annotation.NonNull;

import com.cn.frame.utils.SweetLog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dundun
 */
public class NotifyChangeListenerManager {
    private static NotifyChangeListenerServer notifyChangeListenerServer;

    public synchronized static NotifyChangeListenerServer getInstance() {
        if (notifyChangeListenerServer == null) {
            notifyChangeListenerServer = new NotifyChangeListenerServer();
        }
        return notifyChangeListenerServer;
    }

    public static class NotifyChangeListenerServer implements INotifyChangeListenerServer {
        private final String TAG = "Sweet->notify";
        /**
         * 关注数
         */
        private final List<IChange<String>> mFollowNumChangeListeners =
                new CopyOnWriteArrayList<>();
        /**
         * 关注列表
         */
        private final List<IChange<String>> mFollowListChangeListeners =
                new CopyOnWriteArrayList<>();
        /**
         * 关注列表
         */
        private final List<IChange<String>> mRobotMessageListeners =
                new CopyOnWriteArrayList<>();
        /**
         * 服务协议更新
         */
        private final List<IChange<String>> mProtocolChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 医生认证
         */
        private final List<IChange<Integer>> mDoctorAuthStatusChangeListeners =
                new CopyOnWriteArrayList<>();
        /**
         * 转诊申请
         */
        private final List<IChange<String>> mDoctorTransferPatientListeners =
                new CopyOnWriteArrayList<>();
        /**
         * 消息
         */
        private final List<IChange<String>> mRegisterMessageChangeListener =
                new CopyOnWriteArrayList<>();
        /**
         * 消息
         */
        private final List<IChange<String>> mRegisterSystemMessageChangeListener =
                new CopyOnWriteArrayList<>();
        /**
         * 单条消息消息
         */
        private final List<IChange<String>> mRegisterSingleMessageChangeListener =
                new CopyOnWriteArrayList<>();
        /**
         * 服务包订单
         */
        private final List<IChange<String>> mOrderStatusChangeListener =
                new CopyOnWriteArrayList<>();

        @Override
        public void registerFollowNumChangeListener(@NonNull IChange<String> listener,
                                                    @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mFollowNumChangeListeners.add(listener);
            } else {
                mFollowNumChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerFollowList(@NonNull IChange<String> listener,
                                       @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mFollowListChangeListeners.add(listener);
            } else {
                mFollowListChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerRobotMessage(@NonNull IChange<String> listener,
                                         @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mRobotMessageListeners.add(listener);
            } else {
                mRobotMessageListeners.remove(listener);
            }
        }

        @Override
        public void registerProtocolChangeListener(@NonNull IChange<String> listener,
                                                   @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mProtocolChangeListeners.add(listener);
            } else {
                mProtocolChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorTransferPatientListener(@NonNull IChange<String> listener,
                                                          @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mDoctorTransferPatientListeners.add(listener);
            } else {
                mDoctorTransferPatientListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorAuthStatusChangeListener(@NonNull IChange<Integer> listener,
                                                           @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mDoctorAuthStatusChangeListeners.add(listener);
            } else {
                mDoctorAuthStatusChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerMessageStatusChangeListener(@NonNull IChange<String> listener,
                                                        @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mRegisterMessageChangeListener.add(listener);
            } else {
                mRegisterMessageChangeListener.remove(listener);
            }
        }

        @Override
        public void registerSystemMessageStatusChangeListener(@NonNull IChange<String> listener,
                                                              @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mRegisterSystemMessageChangeListener.add(listener);
            } else {
                mRegisterSystemMessageChangeListener.remove(listener);
            }
        }

        @Override
        public void registerSingleMessageStatusChangeListener(@NonNull IChange<String> listener,
                                                              @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mRegisterSingleMessageChangeListener.add(listener);
            } else {
                mRegisterSingleMessageChangeListener.remove(listener);
            }
        }

        @Override
        public void registerOrderStatusChangeListener(@NonNull IChange<String> listener,
                                                      @NonNull RegisterType registerType) {
            if (RegisterType.REGISTER == registerType) {
                mOrderStatusChangeListener.add(listener);
            } else {
                mOrderStatusChangeListener.remove(listener);
            }
        }

        /**
         * 关注
         */
        public void notifyFollowNumChanged(final String data) {
            synchronized (mFollowNumChangeListeners) {
                for (int i = 0, size = mFollowNumChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mFollowNumChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyStatusChange error", e);
                    }
                }
            }
        }

        /**
         * 关注列表
         */
        public void notifyFollowListChange(final String data) {
            synchronized (mFollowListChangeListeners) {
                for (int i = 0, size = mFollowListChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mFollowListChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyStatusChange error", e);
                    }
                }
            }
        }

        /**
         * 关注列表
         */
        public void notifyRobotMessage(final String data) {
            synchronized (mRobotMessageListeners) {
                for (int i = 0, size = mRobotMessageListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mRobotMessageListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyStatusChange error", e);
                    }
                }
            }
        }

        /**
         * 医生添加
         */
        public void notifyProtocolChange(final String data) {
            synchronized (mProtocolChangeListeners) {
                for (int i = 0, size = mProtocolChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mProtocolChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 转诊申请
         */
        public void notifyDoctorTransferPatient(final String data) {
            synchronized (mDoctorTransferPatientListeners) {
                for (int i = 0, size = mDoctorTransferPatientListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mDoctorTransferPatientListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 医生认证状态
         */
        public void notifyDoctorAuthStatus(final Integer data) {
            synchronized (mDoctorAuthStatusChangeListeners) {
                for (int i = 0, size = mDoctorAuthStatusChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<Integer> change = mDoctorAuthStatusChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 消息
         */
        public void notifyMessageStatusChange(final String data) {
            synchronized (mRegisterMessageChangeListener) {
                for (int i = 0, size = mRegisterMessageChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mRegisterMessageChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 系统消息
         */
        public void notifySystemMessageStatusChange(final String data) {
            synchronized (mRegisterSystemMessageChangeListener) {
                for (int i = 0, size = mRegisterSystemMessageChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mRegisterSystemMessageChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 单条消息
         */
        public void notifySingleMessageStatusChange(final String data) {
            synchronized (mRegisterSingleMessageChangeListener) {
                for (int i = 0, size = mRegisterSingleMessageChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mRegisterSingleMessageChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 服务包订单
         */
        public void notifyOrderStatusChange(final String data) {
            synchronized (mOrderStatusChangeListener) {
                for (int i = 0, size = mOrderStatusChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mOrderStatusChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        SweetLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }
    }
}
