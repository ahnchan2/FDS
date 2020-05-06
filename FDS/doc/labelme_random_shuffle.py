#!/usr/bin/env python

import json
import random
import shutil
import glob
import json
import os
import os.path as osp

def data_copy(json_file_path_name, ROOT_DIR, OUT_DIR, SUB_OUT_DIR):

    with open(json_file_path_name) as f:
        label_data = json.load(f)
        image_path = label_data["imagePath"]

        file_name_ext = os.path.splitext(image_path)

        img_file_path_name = os.path.join(ROOT_DIR, image_path)
        # print(" file_name : {} , json_path_name : {}".format(image_path, image_path))

        json_copy_path_name = os.path.join(OUT_DIR, SUB_OUT_DIR, file_name_ext[0]+".json")
        img_copy_path_name = os.path.join(OUT_DIR, SUB_OUT_DIR, image_path)

        # print(" json_file_path_name : {}".format(json_file_path_name))
        # print(" img_file_path_name : {}".format(img_file_path_name))
        #
        # print(" json_copy_path_name : {}".format(json_copy_path_name))
        # print(" img_copy_path_name : {}".format(img_copy_path_name))

        shutil.copy(json_file_path_name, json_copy_path_name)
        shutil.copy(img_file_path_name, img_copy_path_name)


def random_shuffle(ROOT_DIR, train_ratio=0.6):
    label_files = glob.glob(osp.join(ROOT_DIR, '*.json'))
    string_label_dict = dict()
    string_label_id = []

    for image_id, label_file in enumerate(label_files):
        # print(" image_id : {} , label_file : {}".format(image_id, label_file))
        string_label_dict[image_id] = label_file
        string_label_id.append(image_id)

    random.shuffle(string_label_id)  # shuffle method

    string_label_id_len = len(string_label_id)

    test_ratio = val_ratio = round(float((1 - train_ratio) / 2), 2)

    print(" test_ratio : {}".format(test_ratio))

    val_list = random.sample(string_label_id, int(string_label_id_len*val_ratio))
    for sample in val_list:
        string_label_id.remove(sample)

    test_list = random.sample(string_label_id, int(string_label_id_len * test_ratio))
    for sample in test_list:
        string_label_id.remove(sample)

    train_list = random.sample(string_label_id, len(string_label_id))
    # for sample in train_list:
    #     string_label_id.remove(sample)

    return train_list, val_list, test_list, string_label_dict

if __name__ == '__main__':

    # ROOT_DIR = '/media/ryu/0e86ffd8-8f86-457d-aed1-4bfb927768ac/home/ryu/study_project/data/shelf_life_mobile/read_cj_shelf_life_0330/all'
    # OUT_DIR = '/media/ryu/0e86ffd8-8f86-457d-aed1-4bfb927768ac/home/ryu/study_project/data/shelf_life_mobile/read_cj_shelf_life_0330/new'

    ROOT_DIR = 'D:/333_youtube_detection/youtube_test_rename'
    OUT_DIR = 'D:/333_youtube_detection/youtube_test_rename_shu'

    train_list, val_list, test_list, string_label_dict = random_shuffle(ROOT_DIR, train_ratio=0.8)

    for train_id in train_list:
        print("==================== train_id : {} ".format(train_id))
        json_file_path_name = string_label_dict[train_id]
        SUB_OUT_DIR = 'train'
        data_copy(json_file_path_name, ROOT_DIR, OUT_DIR, SUB_OUT_DIR)

    for val_id in val_list:
        print("==================== val_id : {} ".format(val_id))
        json_file_path_name = string_label_dict[val_id]
        SUB_OUT_DIR = 'val'
        data_copy(json_file_path_name, ROOT_DIR, OUT_DIR, SUB_OUT_DIR)

    for test_id in test_list:
        print("==================== test_id : {} ".format(test_id))
        json_file_path_name = string_label_dict[test_id]
        SUB_OUT_DIR = 'test'
        data_copy(json_file_path_name, ROOT_DIR, OUT_DIR, SUB_OUT_DIR)

