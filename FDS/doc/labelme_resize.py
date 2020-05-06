#!/usr/bin/env python

import argparse
import glob
import json
import os, cv2
import os.path as osp
import sys
import uuid
import base64

import shutil

def resize_img(IMAGE_DIR, RESCALE_DIR, JSON_DIR):
    # base = 600 # Youtube
    # base = 300 # Shelf_life
    base = 600  # TEST
    ENCODING = "UTF-8"
    count_recale = 0
    count_copy = 0
    for path, dirs, files in os.walk(IMAGE_DIR):
        # print("path s: ", path, " , dirs : ", dirs, ", txtfiles : ", files)

        for file_name in files:
            img_file_path_name = os.path.join(IMAGE_DIR, file_name)
            print('file_name : {}'.format(file_name))

            image_rescale = cv2.imread(img_file_path_name)

            h, w = image_rescale.shape[0:2]
            print(" W : {} , H : {}".format(w, h))

            file_name_ext = os.path.splitext(file_name)
            file_name_json = file_name_ext[0] + ".json"
            print(" file_name_ext : {} , file_name_json : {}".format(file_name_ext, file_name_json))
            json_file_path_name = os.path.join(JSON_DIR, file_name_json)
            rescaled_json_name = os.path.join(RESCALE_DIR, file_name_json)
            rescaled_path_name = os.path.join(RESCALE_DIR, file_name)
            if h < w:
                ratio = float(base / w)
            else:
                ratio = float(base / h)

            print(" base : {} , w : {}, h : {}, ratio : {}".format(base, w, h, ratio))
            if ratio < 1:
                h_rescale = int(h * ratio)
                w_rescale = int(w * ratio)

                with open(json_file_path_name, encoding="UTF-8") as f:
                    label_data = json.load(f)
                    # print('##################### label_data : {}'.format(label_data))

                    shapes = label_data["shapes"]
                    shapes_len = len(shapes)
                    # print('##################### label_data shapes : {}'.format(shapes))

                    for i in range(shapes_len):
                        print('##################### label_data shape : {}'.format(label_data["shapes"][i]))
                        points = label_data["shapes"][i]["points"]
                        points_len = len(points)
                        for j in range(points_len):
                            point_values = label_data["shapes"][i]["points"][j]
                            point_values_x = point_values[0]
                            point_values_y = point_values[1]

                            label_data["shapes"][i]["points"][j][0] = float(point_values_x * ratio)
                            label_data["shapes"][i]["points"][j][1] = float(point_values_y * ratio)

                    # point_values = label_data["shapes"][0]["points"][0]
                    # point_values_x = label_data["shapes"][0]["points"][0][0]
                    # point_values_y = label_data["shapes"][0]["points"][0][1]
                    # label_data["shapes"][0]["points"][0][0] = float(point_values_x * ratio)
                    # label_data["shapes"][0]["points"][0][1] = float(point_values_y * ratio)
                    # point_values_len = len(point_values)
                    # if point_values_len > 2:
                    #     print(">>>>>>>>>>>>>> img_file_path_name : {}".format(img_file_path_name))

                    print('##################### label_data point_values : {}'.format(point_values))
                    print("imageHeight : {} ".format(label_data["imageHeight"]))
                    print("imageWidth : {} ".format(label_data["imageWidth"]))

                    label_data["imageHeight"] = h_rescale
                    label_data["imageWidth"] = w_rescale

                    image_rescaleed = cv2.resize(image_rescale, dsize=(w_rescale, h_rescale),
                                                 interpolation=cv2.INTER_AREA)
                    cv2.imwrite(rescaled_path_name, image_rescaleed)
                    count_recale = count_recale + 1

                    bytes = None
                    with open(rescaled_path_name, "rb") as imageFile:
                        str = base64.b64encode(imageFile.read())
                        bytes = str.decode("UTF-8")

                    label_data["imageData"] = bytes
                with open(rescaled_json_name, 'w', encoding='utf-8') as make_file:
                    json.dump(label_data, make_file, indent="\t")


                print("recale : {} ".format("###################################"))
            else:
                shutil.copy(img_file_path_name, rescaled_path_name)
                shutil.copy(json_file_path_name, rescaled_json_name)
                count_copy = count_copy + 1
                print("copy : {} ".format("###################################"))


            # image_rescaled = cv2.resize(image_rescale, dsize=(0, 0), fx=0.5, fy=0.5, interpolation=cv2.INTER_LINEAR)
            # rescaled_name = file_name.replace(".png", "_rescale.png")
            # rescaled_path_name = os.path.join(IMAGE_DIR_RESCALE, rescaled_name)
            # cv2.imwrite(rescaled_path_name, image_rescaled)

            # image_rescale = skimage.io.imread(img_file_path_name)
            # image_rescaled = rescale(image_rescale, 0.5, anti_aliasing=False)
            # rescaled_name = file_name.replace(".JPG", "_rescale.JPG")
            # rescaled_path_name = os.path.join(IMAGE_DIR_RESCALE, rescaled_name)
            # img_uint8 = image_rescaled.astype(np.uint8)
            # skimage.io.imsave(rescaled_path_name, img_uint8)
    print("count_recale : {}, count_copy : {} ".format(count_recale, count_copy))

if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    parser.add_argument('--img_input_dir', help='input annotated directory')
    parser.add_argument('--json_input_dir', help='input annotated directory')
    parser.add_argument('--output_dir', help='output dataset directory')
    args = parser.parse_args()

    IMAGE_DIR = 'D:/111_detection/imgs_train_add'
    RESCALE_DIR = 'D:/111_detection/train_add_test2'
    JSON_DIR = 'D:/111_detection/jsons_train_add'

    IMAGE_DIR = args.img_input_dir
    JSON_DIR = args.json_input_dir
    RESCALE_DIR = args.output_dir
    # IMAGE_DIR = 'D:/333_youtube_detection/imgs_train'
    # RESCALE_DIR = 'D:/333_youtube_detection/train'
    # JSON_DIR = 'D:/333_youtube_detection/jsons_train'
    # IMAGE_DIR = '/media/ryu/0e86ffd8-8f86-457d-aed1-4bfb927768ac/home/ryu/study_project/data/shelf_life_mobile/111'
    # RESCALE_DIR = '/media/ryu/0e86ffd8-8f86-457d-aed1-4bfb927768ac/home/ryu/study_project/data/shelf_life_mobile/img_resize'
    # JSON_DIR = '/media/ryu/0e86ffd8-8f86-457d-aed1-4bfb927768ac/home/ryu/study_project/data/shelf_life_mobile/222'
    resize_img(IMAGE_DIR, RESCALE_DIR, JSON_DIR)
